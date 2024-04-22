package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.entity.SessaoVotacao;
import cooperativa.votacao.enums.StatusVotacao;
import cooperativa.votacao.repository.PautaRepository;
import cooperativa.votacao.repository.SessaoVotacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class SessaoVotacaoServiceTest {
    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    @MockBean
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @MockBean
    private PautaRepository pautaRepository;

    @MockBean
    private PautaService pautaService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldOpenVotingSessionWithDefaultDuration() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Pauta 1");
        pauta.setDetalhes("Detalhes pauta 1");
        Mockito.when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        SessaoVotacao sessaoVotacao = new SessaoVotacao(pauta, null);
        Mockito.when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessaoVotacao);

        SessaoVotacao result = sessaoVotacaoService.abrirSessao(1L, null);

        assertNotNull(result);
        assertEquals(Duration.ofMinutes(1), result.getDuracao());
        verify(sessaoVotacaoRepository).save(any(SessaoVotacao.class));
    }

    @Test
    void shouldOpenVotingSessionWithCustomDuration() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Pauta 1");
        pauta.setDetalhes("Detalhes pauta 1");
        Mockito.when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        SessaoVotacao sessaoVotacao = new SessaoVotacao(pauta, Duration.ofMinutes(10));
        Mockito.when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessaoVotacao);

        SessaoVotacao result = sessaoVotacaoService.abrirSessao(1L, Duration.ofMinutes(10));

        assertNotNull(result);
        assertEquals(Duration.ofMinutes(10), result.getDuracao());
        verify(sessaoVotacaoRepository).save(any(SessaoVotacao.class));
    }

    @Test
    void shouldThrowExceptionWhenPautaNotFound() {
        Mockito.when(pautaService.getPautaById(1L)).thenThrow(new RuntimeException("Pauta não encontrada"));

        assertThrows(RuntimeException.class, () -> sessaoVotacaoService.abrirSessao(1L, Duration.ofMinutes(1)));
    }

    @Test
    void shouldThrowExceptionWhenSaveFails() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        Mockito.when(pautaService.getPautaById(1L)).thenReturn(pauta);

        Mockito.when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(null);

        assertThrows(RuntimeException.class, () -> sessaoVotacaoService.abrirSessao(1L, Duration.ofMinutes(1)));
    }

    @Test
    void shouldReturnActiveSessionWhenExists() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setStatus(StatusVotacao.ABERTA);
        Mockito.when(sessaoVotacaoRepository.findById(anyLong())).thenReturn(Optional.of(sessaoVotacao));

        SessaoVotacao result = sessaoVotacaoService.findActiveByPautaId(1L);

        assertNotNull(result);
        assertEquals(StatusVotacao.ABERTA, result.getStatus());
    }

    @Test
    void shouldReturnNullWhenNoActiveSessionExists() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setStatus(StatusVotacao.FECHADA);
        Mockito.when(sessaoVotacaoRepository.findById(anyLong())).thenReturn(Optional.of(sessaoVotacao));

        SessaoVotacao result = sessaoVotacaoService.findActiveByPautaId(1L);

        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenNoSessionExists() {
        Mockito.when(sessaoVotacaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        SessaoVotacao result = sessaoVotacaoService.findActiveByPautaId(1L);

        assertNull(result);
    }

    @Test
    void shouldThrowExceptionWhenActiveSessionAlreadyExistsForPauta() {
        long pautaId = 1L;
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setStatus(StatusVotacao.ABERTA);
        Mockito.when(sessaoVotacaoRepository.findById(pautaId)).thenReturn(Optional.of(sessaoVotacao));

        assertThrows(RuntimeException.class, () -> sessaoVotacaoService.abrirSessao(pautaId, Duration.ofMinutes(1)));
    }

    @Test
    void shouldCloseVotingSessionAfterDuration() throws InterruptedException {
        // Arrange
        long pautaId = 1L;
        Duration duration = Duration.ofSeconds(1);
        Pauta pauta = new Pauta();
        pauta.setId(pautaId);
        Mockito.when(pautaService.getPautaById(pautaId)).thenReturn(pauta);

        SessaoVotacao sessaoVotacao = new SessaoVotacao(pauta, duration);
        Mockito.when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessaoVotacao);

        Mockito.when(sessaoVotacaoRepository.findById(pautaId)).thenReturn(Optional.empty());

        // Act
        SessaoVotacao result = sessaoVotacaoService.abrirSessao(pautaId, duration);

        // adiciona pequeno buffer para garantir que a execução do scheduler foi concluída
        Thread.sleep(duration.plusMillis(500).toMillis()); // Change this to 500 milliseconds

        //Assert
        // verifica se o método save foi chamado duas vezes (uma pra criar, outra pra dar o update)
        Mockito.verify(sessaoVotacaoRepository, times(2)).save(any(SessaoVotacao.class));

        // verifica se o status foi alterado para FECHADA
        assertEquals(StatusVotacao.FECHADA, result.getStatus());
    }
}