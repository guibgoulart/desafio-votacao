package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.entity.SessaoVotacao;
import cooperativa.votacao.entity.StatusVotacao;
import cooperativa.votacao.repository.PautaRepository;
import cooperativa.votacao.repository.SessaoVotacaoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@Log4j2
public class SessaoVotacaoService {

    @Autowired
    SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    PautaService pautaService;

    public SessaoVotacao abrirSessao(long pautaId, Duration duracao) {
        try {
            log.info("Abrindo sessão de votação para pauta: {}", pautaId);
            Pauta pauta = pautaService.getPautaById(pautaId);
            SessaoVotacao sessaoVotacao = new SessaoVotacao(pauta, duracao);

            SessaoVotacao savedSessaoVotacao = sessaoVotacaoRepository.save(sessaoVotacao);

            // verificar se teve sucesso e logar informaçoes
            if (savedSessaoVotacao == null) {
                log.error("Falha ao salvar a sessão de votação para a pauta: {}", pautaId);
                throw new RuntimeException("Falha ao salvar a sessão de votação");
            } else {
                log.info("Sessão de votação {} foi aberta com sucesso", savedSessaoVotacao);
            }
            return savedSessaoVotacao;
        } catch (Exception e) {
            log.error("Erro ao abrir sessão de votação: {}", e.getMessage());
            throw new RuntimeException("Erro ao abrir sessão de votação", e);
        }
    }

    public SessaoVotacao findActiveByPautaId(long pautaId) {
        return sessaoVotacaoRepository.findById(pautaId).filter(sv -> sv.getStatus() == StatusVotacao.ABERTA).orElse(null);
    }
}
