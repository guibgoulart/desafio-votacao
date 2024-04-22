package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.entity.SessaoVotacao;
import cooperativa.votacao.entity.Voto;
import cooperativa.votacao.enums.ResultadoPauta;
import cooperativa.votacao.repository.VotoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class VotoService {
    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private PautaService pautaService;

    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    public Voto votar(Long pautaId, Long associadoId, Voto.TipoVoto tipoVoto) {

        log.info("Votando na pauta: {}, associado: {}, voto: {}", pautaId, associadoId, tipoVoto);

        Pauta pauta = pautaService.getPautaById(pautaId);

        SessaoVotacao sessaoVotacao = sessaoVotacaoService.findActiveByPautaId(pautaId);
        if (sessaoVotacao == null) {
            log.error("Sessão de votação não está ativa");
            throw new IllegalArgumentException("Sessão de votação não está ativa");
        }

        // verifica se associado já votou nessa pauta
        Optional<Voto> votoOptional = votoRepository.findById(associadoId);
        if (votoOptional.isPresent() && votoOptional.get().getPauta().getId().equals(pautaId)) {
            log.error("Associado já votou nessa Pauta");
            throw new IllegalArgumentException("Associado já votou nessa Pauta");
        }

        Voto voto = new Voto();
        voto.setPauta(pauta);
        voto.setAssociadoId(associadoId);
        voto.setTipoVoto(tipoVoto);
        return votoRepository.save(voto);
    }

    public ResultadoVotacao getResultadoVotacao(Long pautaId) {
        log.info("Buscando resultado da votação para pauta: {}", pautaId);
        List<Voto> votos = votoRepository.findAll().stream().filter(voto -> voto.getPauta().getId().equals(pautaId)).toList();

        long totalSim = votos.stream().filter(voto -> voto.getTipoVoto() == Voto.TipoVoto.SIM).count();
        long totalNao = votos.stream().filter(voto -> voto.getTipoVoto() == Voto.TipoVoto.NAO).count();

        ResultadoVotacao resultado = new ResultadoVotacao();
        resultado.setTotalSim(totalSim);
        resultado.setTotalNao(totalNao);

        return resultado;
    }

    public ResultadoPauta resultadoPauta(Long pautaId) {
        log.info("Verificando se pauta {} foi aprovada...", pautaId);
        ResultadoVotacao resultado = getResultadoVotacao(pautaId);

        if(sessaoVotacaoService.findActiveByPautaId(pautaId) != null) {
            log.error("Sessão de votação ainda está ativa");
            throw new IllegalArgumentException("Sessão de votação ainda está ativa");
        }

        if (resultado.getTotalSim() > resultado.getTotalNao()) {
            return ResultadoPauta.APROVADA;
        } else if (resultado.getTotalSim() < resultado.getTotalNao()) {
            return ResultadoPauta.REJEITADA;
        } else {
            return ResultadoPauta.EMPATE;
        }
    }
}

