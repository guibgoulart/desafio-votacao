package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.entity.SessaoVotacao;
import cooperativa.votacao.enums.StatusVotacao;
import cooperativa.votacao.repository.SessaoVotacaoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class SessaoVotacaoService {

    @Autowired
    SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    PautaService pautaService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SessaoVotacao abrirSessao(long pautaId, Duration duracao) {
        try {
            log.info("Abrindo sessão de votação para pauta: {}", pautaId);
            Pauta pauta = pautaService.getPautaById(pautaId);
            SessaoVotacao sessaoVotacao = new SessaoVotacao(pauta, duracao);

            if(findActiveByPautaId(pautaId) != null) {
                log.error("Já existe uma sessão de votação aberta para a pauta: {}", pautaId);
                throw new RuntimeException("Já existe uma sessão de votação aberta");
            }

            SessaoVotacao savedSessaoVotacao = sessaoVotacaoRepository.save(sessaoVotacao);

            scheduler.schedule(() -> {
                log.info("Fechando sessão de votação para pauta: {}", pautaId);
                savedSessaoVotacao.setStatus(StatusVotacao.FECHADA);
                sessaoVotacaoRepository.save(savedSessaoVotacao);
                log.info("Sessão de votação fechada para pauta: {}", pautaId);
            }, duracao.toMinutes(), TimeUnit.MINUTES);

            // verificar se teve sucesso e logar informaçoes
            if (savedSessaoVotacao == null) {
                log.error("Falha ao salvar a sessão de votação para a pauta: {}", pautaId);
                throw new RuntimeException("Falha ao salvar a sessão de votação");
            } else {
                log.info("Sessão de votação {} foi aberta com sucesso", savedSessaoVotacao.toString());
            }
            return savedSessaoVotacao;
        } catch (Exception e) {
            log.error("Erro ao abrir sessão de votação: {}", e.getMessage());
            throw new RuntimeException("Erro ao abrir sessão de votação", e);
        }
    }

    public SessaoVotacao findActiveByPautaId(long pautaId) {
        return sessaoVotacaoRepository.findById(pautaId).orElse(null);
    }
}