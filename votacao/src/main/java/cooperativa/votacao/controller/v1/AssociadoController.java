package cooperativa.votacao.controller.v1;

import cooperativa.votacao.entity.Associado;
import cooperativa.votacao.service.AssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/associados")
public class AssociadoController {

    @Autowired
    private AssociadoService associadoService;

    @PostMapping
    public ResponseEntity<Associado> createAssociado(@RequestBody Associado associado) {
        try {
            Associado createdAssociado = associadoService.createAssociado(associado);
            return new ResponseEntity<>(createdAssociado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}