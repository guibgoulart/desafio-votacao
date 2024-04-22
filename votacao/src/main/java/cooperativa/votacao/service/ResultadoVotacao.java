package cooperativa.votacao.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResultadoVotacao {
    private long totalSim;
    private long totalNao;
}
