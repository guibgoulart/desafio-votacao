package cooperativa.votacao.service;

import cooperativa.votacao.entity.Associado;
import cooperativa.votacao.repository.AssociadoRepository;
import cooperativa.votacao.service.AssociadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AssociadoServiceTest {

    @InjectMocks
    AssociadoService associadoService;

    @Mock
    AssociadoRepository associadoRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createAssociadoSuccessfully() throws Exception {
        Associado associado = new Associado();
        when(associadoRepository.save(any(Associado.class))).thenReturn(associado);

        Associado result = associadoService.createAssociado(associado);

        assertEquals(associado, result);
        verify(associadoRepository, times(1)).save(associado);
    }

    @Test
    public void createAssociadoThrowsException() {
        Associado associado = new Associado();
        when(associadoRepository.save(any(Associado.class))).thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> associadoService.createAssociado(associado));
        verify(associadoRepository, times(1)).save(associado);
    }
}