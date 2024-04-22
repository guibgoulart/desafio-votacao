package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.entity.SessaoVotacao;
import cooperativa.votacao.repository.PautaRepository;
import cooperativa.votacao.repository.SessaoVotacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SessaoVotacaoService {

    @Autowired
    SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    PautaService pautaService;

    public SessaoVotacao abrirSessao(long pautaId, Duration duracao) {
        Pauta pauta = pautaService.getPautaById(pautaId);
        return sessaoVotacaoRepository.save(new SessaoVotacao(pauta, duracao));
    }
}
