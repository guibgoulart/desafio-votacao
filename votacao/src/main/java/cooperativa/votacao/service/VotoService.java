package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.entity.SessaoVotacao;
import cooperativa.votacao.entity.Voto;
import cooperativa.votacao.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VotoService {
    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private PautaService pautaService;

    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    public Voto votar(Long pautaId, Long associadoId, Voto.TipoVoto tipoVoto) {
        Pauta pauta = pautaService.getPautaById(pautaId);

        SessaoVotacao sessaoVotacao = sessaoVotacaoService.findActiveByPautaId(pautaId);
        if (sessaoVotacao == null) {
            throw new IllegalArgumentException("Sessão de votação não está ativa");
        }

        // verifica se associado já votou nessa pauta
        Optional<Voto> votoOptional = votoRepository.findById(associadoId);
        if (votoOptional.isPresent() && votoOptional.get().getPauta().getId().equals(pautaId)) {
            throw new IllegalArgumentException("Associado já votou nessa Pauta");
        }

        Voto voto = new Voto();
        voto.setPauta(pauta);
        voto.setAssociadoId(associadoId);
        voto.setTipoVoto(tipoVoto);
        return votoRepository.save(voto);
    }
}
