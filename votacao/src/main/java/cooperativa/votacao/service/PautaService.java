package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.repository.PautaRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    public Pauta createPauta(Pauta pauta) throws Exception {
        log.trace("Criando pauta: {}", pauta);
        try {
            return pautaRepository.save(pauta);
        } catch (Exception e) {
            log.error("Erro ao criar pauta: {}", e.getMessage());
            throw new Exception("Erro ao criar pauta", e);
        }
    }
}