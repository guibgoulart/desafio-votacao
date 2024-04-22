package cooperativa.votacao.controller.v1;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.enums.ResultadoPauta;
import cooperativa.votacao.service.PautaService;
import cooperativa.votacao.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @Autowired
    private VotoService votoService;

    @PostMapping
    public ResponseEntity<Pauta> createPauta(@RequestBody Pauta pauta) {
        try {
            Pauta createdPauta = pautaService.createPauta(pauta);
            return new ResponseEntity<>(createdPauta, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{pautaId}/resultado")
    public ResponseEntity<ResultadoPauta> getResultadoVotacao(@PathVariable long pautaId) {
        try {
            ResultadoPauta resultado = votoService.resultadoPauta(pautaId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}