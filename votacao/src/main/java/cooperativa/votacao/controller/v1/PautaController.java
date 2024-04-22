package cooperativa.votacao.controller.v1;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @PostMapping
    public ResponseEntity<Pauta> createPauta(@RequestBody Pauta pauta) {
        try {
            Pauta createdPauta = pautaService.createPauta(pauta);
            return new ResponseEntity<>(createdPauta, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}