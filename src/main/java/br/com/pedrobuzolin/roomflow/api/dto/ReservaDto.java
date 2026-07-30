package br.com.pedrobuzolin.roomflow.api.dto;

import br.com.pedrobuzolin.roomflow.api.model.Reserva;
import br.com.pedrobuzolin.roomflow.api.model.StatusReserva;

import java.time.LocalDateTime;

public record ReservaDto(Long id, SalaDto sala, UsuarioDto usuario, LocalDateTime dataInicio, LocalDateTime dataFim, StatusReserva statusReserva) {
    public ReservaDto(Reserva reserva) {this(reserva.getId(), new SalaDto(reserva.getSala()), new UsuarioDto(reserva.getUsuario()),
            reserva.getDataInicio(), reserva.getDataFim(), reserva.getStatusReserva());}
}
