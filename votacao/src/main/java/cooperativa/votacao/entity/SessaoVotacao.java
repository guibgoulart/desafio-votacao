package cooperativa.votacao.entity;

import cooperativa.votacao.enums.StatusVotacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class SessaoVotacao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    private LocalDateTime inicio;

    private Duration duracao;

    @Enumerated(EnumType.STRING)
    private StatusVotacao status;

    public SessaoVotacao(Pauta pauta, Duration duracao) {
        this.pauta = pauta;
        this.inicio = LocalDateTime.now();
        this.duracao = duracao != null ? duracao : Duration.ofMinutes(1);
        this.status = StatusVotacao.ABERTA;
    }
}