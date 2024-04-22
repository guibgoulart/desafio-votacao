package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.entity.SessaoVotacao;
import cooperativa.votacao.entity.StatusVotacao;
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
}