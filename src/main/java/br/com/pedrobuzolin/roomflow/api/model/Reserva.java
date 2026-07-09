package br.com.pedrobuzolin.roomflow.api.model;

import jakarta.persistence.*;

import java.time.LocalDate;

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
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Reserva() {}
}
