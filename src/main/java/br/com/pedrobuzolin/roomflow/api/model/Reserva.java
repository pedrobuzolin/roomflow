package br.com.pedrobuzolin.roomflow.api.model;

import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private StatusReserva statusReserva;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY)
    private Sala sala;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    public Reserva() {}

    public Reserva (Sala sala, Usuario usuario, LocalDateTime dataInicio, LocalDateTime dataFim) {
        validarHorarios(dataInicio, dataFim);

        this.sala = sala;
        this.usuario = usuario;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.statusReserva = StatusReserva.ATIVA;
    }

    public Long getId() {
        return id;
    }

    public StatusReserva getStatusReserva() {
        return statusReserva;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void marcarReservaComoCancelada () {
        this.statusReserva = StatusReserva.CANCELADA;
    }

    private void validarHorarios(LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (!dataInicio.isBefore(dataFim)) {
            throw new ValidacaoException("A Data de Inicio deve ser antes da Data de Fim");
        }
    }
}
