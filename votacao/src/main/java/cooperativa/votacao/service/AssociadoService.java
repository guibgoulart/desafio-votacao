package cooperativa.votacao.service;

import cooperativa.votacao.entity.Associado;
import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.repository.AssociadoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AssociadoService {

    @Autowired
    AssociadoRepository associadoRepository;

    public Associado createAssociado(Associado associado) throws Exception {
        log.info("Criando associado: {}", associado.toString());
        try {
            Associado novoAssociado = associadoRepository.save(associado);
            log.info("Associado criado com sucesso: {}", novoAssociado.toString());
            return novoAssociado;
        } catch (Exception e) {
            log.error("Erro ao criar associado: {}", e.getMessage());
            throw new Exception("Erro ao criar associado", e);
        }
    }
}
