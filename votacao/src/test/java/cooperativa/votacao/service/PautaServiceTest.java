package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.ExpectedCount.times;

@SpringBootTest
public class PautaServiceTest {

    @Autowired
    private PautaService pautaService;

    @MockBean
    private PautaRepository pautaRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void shouldCreatePautaSuccesfully() {
        // Arrange
        Pauta pauta = new Pauta();
        pauta.setTitulo("Pauta 1");
        pauta.setDetalhes("Detalhes pauta 1");
        Mockito.when(pautaRepository.save(pauta)).thenReturn(pauta);

        //Act
        Pauta pautaCriada = pautaService.createPauta(pauta);

        //Assert
        assertNotNull(pautaCriada);
        assertEquals(pauta.getTitulo(), pautaCriada.getTitulo());
        assertEquals(pauta.getDetalhes(), pautaCriada.getDetalhes());
        verify(pautaRepository).save(pauta);
    }
}