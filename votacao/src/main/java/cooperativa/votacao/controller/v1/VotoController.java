package cooperativa.votacao.controller.v1;

import cooperativa.votacao.entity.Voto;
import cooperativa.votacao.enums.TipoVoto;
import cooperativa.votacao.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/votos")
public class VotoController {

    @Autowired
    private VotoService votoService;

    @PostMapping("/{pautaId}/{associadoId}")
    public ResponseEntity<Voto> votar(@PathVariable long pautaId, @PathVariable long associadoId, @RequestParam String voto) {
        try {
            TipoVoto tipoVoto = TipoVoto.valueOf(voto.toUpperCase());
            Voto votoCriado = votoService.votar(pautaId, associadoId, tipoVoto);
            return new ResponseEntity<>(votoCriado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}