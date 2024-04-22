package cooperativa.votacao.service;

import cooperativa.votacao.entity.Pauta;
import cooperativa.votacao.entity.SessaoVotacao;
import cooperativa.votacao.entity.Voto;
import cooperativa.votacao.enums.ResultadoPauta;
import cooperativa.votacao.enums.TipoVoto;
import cooperativa.votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VotoServiceTest {
    @InjectMocks
    private VotoService votoService;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaService pautaService;

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVotarPautaDoesNotExist() {
        when(pautaService.getPautaById(anyLong())).thenThrow(new RuntimeException("Pauta não encontrada"));

        assertThrows(RuntimeException.class, () -> {
            votoService.votar(1L, 1L, TipoVoto.SIM);
        });
    }

    @Test
    public void testVotarSessaoVotacaoNotActive() {
        when(pautaService.getPautaById(anyLong())).thenReturn(new Pauta());
        when(sessaoVotacaoService.findActiveByPautaId(anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            votoService.votar(1L, 1L, TipoVoto.SIM);
        });
    }

    @Test
    public void testVotarAssociadoAlreadyVoted() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        when(pautaService.getPautaById(anyLong())).thenReturn(pauta);
        when(sessaoVotacaoService.findActiveByPautaId(anyLong())).thenReturn(new SessaoVotacao());

        Voto voto = new Voto();
        voto.setPauta(pauta);
        when(votoRepository.findById(anyLong())).thenReturn(Optional.of(voto));

        assertThrows(IllegalArgumentException.class, () -> {
            votoService.votar(1L, 1L, TipoVoto.SIM);
        });
    }

    @Test
    public void testVotarSuccess() {
        when(pautaService.getPautaById(anyLong())).thenReturn(new Pauta());
        when(sessaoVotacaoService.findActiveByPautaId(anyLong())).thenReturn(new SessaoVotacao());
        when(votoRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(votoRepository.save(any(Voto.class))).thenReturn(new Voto());

        Voto voto = votoService.votar(1L, 1L, TipoVoto.SIM);

        assertNotNull(voto);
    }

    @Test
    public void testGetResultadoVotacao() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);

        Voto votoSim = new Voto();
        votoSim.setPauta(pauta);
        votoSim.setTipoVoto(TipoVoto.SIM);

        Voto votoNao = new Voto();
        votoNao.setPauta(pauta);
        votoNao.setTipoVoto(TipoVoto.NAO);

        when(votoRepository.findAll()
                .stream().filter(voto -> voto.getPauta().getId().equals(anyLong())).toList())
                .thenReturn(Arrays.asList(votoSim, votoNao));

        ResultadoVotacao resultado = votoService.getResultadoVotacao(1L);

        assertEquals(1, resultado.getTotalSim());
        assertEquals(1, resultado.getTotalNao());
    }

    @Test
    public void testResultadoPautaVotacaoAtiva() {
        when(sessaoVotacaoService.findActiveByPautaId(anyLong())).thenReturn(new SessaoVotacao());

        assertThrows(IllegalArgumentException.class, () -> {
            votoService.resultadoPauta(1L);
        });
    }

    @Test
    public void testResultadoPautaAprovada() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);

        Voto votoSim = new Voto();
        votoSim.setPauta(pauta);
        votoSim.setTipoVoto(TipoVoto.SIM);

        Voto votoNao = new Voto();
        votoNao.setPauta(pauta);
        votoNao.setTipoVoto(TipoVoto.NAO);

        when(sessaoVotacaoService.findActiveByPautaId(anyLong())).thenReturn(null);
        when(votoRepository.findAll()
                .stream().filter(voto -> voto.getPauta().getId().equals(anyLong())).toList())
                .thenReturn(Arrays.asList(votoSim, votoSim, votoNao));

        assertEquals(ResultadoPauta.APROVADA, votoService.resultadoPauta(1L));
    }

    @Test
    public void testResultadoPautaRejeitada() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);

        Voto votoSim = new Voto();
        votoSim.setPauta(pauta);
        votoSim.setTipoVoto(TipoVoto.SIM);

        Voto votoNao = new Voto();
        votoNao.setPauta(pauta);
        votoNao.setTipoVoto(TipoVoto.NAO);

        when(sessaoVotacaoService.findActiveByPautaId(anyLong())).thenReturn(null);
        when(votoRepository.findAll()
                .stream().filter(voto -> voto.getPauta().getId().equals(anyLong())).toList())
                .thenReturn(Arrays.asList(votoSim, votoNao, votoNao));

        assertEquals(ResultadoPauta.REJEITADA, votoService.resultadoPauta(1L));
    }

    @Test
    public void testResultadoPautaEmpate() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);

        Voto votoSim = new Voto();
        votoSim.setPauta(pauta);
        votoSim.setTipoVoto(TipoVoto.SIM);

        Voto votoNao = new Voto();
        votoNao.setPauta(pauta);
        votoNao.setTipoVoto(TipoVoto.NAO);

        when(sessaoVotacaoService.findActiveByPautaId(anyLong())).thenReturn(null);
        when(votoRepository.findAll()
                .stream().filter(voto -> voto.getPauta().getId().equals(anyLong())).toList())
                .thenReturn(Arrays.asList(votoSim, votoNao));

        assertEquals(ResultadoPauta.EMPATE, votoService.resultadoPauta(1L));
    }
}