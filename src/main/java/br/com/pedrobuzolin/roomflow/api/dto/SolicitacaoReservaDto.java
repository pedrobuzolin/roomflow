package br.com.pedrobuzolin.roomflow.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SolicitacaoReservaDto(@NotNull Long idSala,
                                    @NotNull Long idUsuario,
                                    @NotNull LocalDateTime dataInicio,
                                    @NotNull LocalDateTime dataFim) {
}
