package br.com.pedrobuzolin.roomflow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AtualizacaoSalaDto(@NotBlank String nome,
                                 @Positive Integer capacidade) {
}
