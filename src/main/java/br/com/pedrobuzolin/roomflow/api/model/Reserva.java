package br.com.pedrobuzolin.roomflow.api.model;

import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="reservas")
@NoArgsConstructor
@Getter
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private StatusReserva statusReserva;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Sala sala;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    public Reserva (Sala sala, Usuario usuario, LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (sala == null || usuario == null) {
            throw new ValidacaoException(
                    "Sala e usuário são obrigatórios para criar uma reserva!"
            );
        }

        validarHorarios(dataInicio, dataFim);

        this.sala = sala;
        this.usuario = usuario;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.statusReserva = StatusReserva.ATIVA;
    }

    public void marcarReservaComoCancelada () {
        if (this.statusReserva == StatusReserva.CANCELADA) {
            throw new ValidacaoException("Reserva já está cancelada!");
        }

        if (!LocalDateTime.now().isBefore(this.dataFim)) {
            throw new ValidacaoException("Não é possível cancelar uma reserva que já encerrada!");
        }

        if (!LocalDateTime.now().isBefore(this.dataInicio)) {
            throw new ValidacaoException("Não é possível cancelar uma reserva que já começou!");
        }

        this.statusReserva = StatusReserva.CANCELADA;
    }

    private void validarHorarios(LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (!dataInicio.isBefore(dataFim)) {
            throw new ValidacaoException("A Data de Inicio deve ser antes da Data de Fim");
        }
    }
}
