package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.entity.SessaoVotacao;
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
import static org.mockito.Mockito.verify;

@SpringBootTest
public class SessaoVotacaoServiceTest {
    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    @MockBean
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @MockBean
    private PautaRepository pautaRepository;

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
}
