package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.repository.PautaRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PautaServiceTest {

    @InjectMocks
    private PautaService pautaService;

    @MockBean
    private PautaRepository pautaRepository;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        MockitoAnnotations.initMocks(this);
    }
    @Test
    void shouldCreatePautaSuccesfully() throws Exception {
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

    @Test
    void shouldFailWhenCreatePautaWithoutTitulo() {
        // Arrange
        Pauta pauta = new Pauta();
        pauta.setDetalhes("Detalhes pauta 1");

        // Act
        var violations = validator.validate(pauta);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")));
    }

    @Test
    void shouldFailWhenCreatePautaWithoutDetalhes() {
        // Arrange
        Pauta pauta = new Pauta();
        pauta.setTitulo("Pauta 1");

        // Act
        var violations = validator.validate(pauta);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("detalhes")));
    }

    @Test
    void shouldThrowExceptionWhenCreatePautaFails() {
        // Arrange
        Pauta pauta = new Pauta();
        pauta.setTitulo("Pauta 1");
        pauta.setDetalhes("Detalhes pauta 1");
        doThrow(new RuntimeException("Erro ao salvar pauta")).when(pautaRepository).save(pauta);

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> pautaService.createPauta(pauta));
        assertEquals("Erro ao criar pauta", exception.getMessage());
    }

    @Test
    public void shouldReturnPautaWhenPautaExists() {
        Pauta pauta = new Pauta();
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        Pauta result = pautaService.getPauta(1L);

        assertEquals(pauta, result);
    }

    @Test
    public void shouldThrowExceptionWhenPautaDoesNotExist() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pautaService.getPauta(1L));
    }
}