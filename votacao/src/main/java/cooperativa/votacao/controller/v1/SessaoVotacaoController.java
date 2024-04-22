package cooperativa.votacao.controller.v1;

import cooperativa.votacao.entity.SessaoVotacao;
import cooperativa.votacao.service.SessaoVotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/sessoes")
public class SessaoVotacaoController {

    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    @PostMapping("/{pautaId}")
    public ResponseEntity<SessaoVotacao> abrirSessao(@PathVariable long pautaId, @RequestParam(required = false) Duration duracao) {
        try {
            SessaoVotacao sessaoVotacao = sessaoVotacaoService.abrirSessao(pautaId, duracao);
            return new ResponseEntity<>(sessaoVotacao, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}