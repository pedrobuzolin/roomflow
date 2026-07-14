package br.com.pedrobuzolin.roomflow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroSalaDto(
        @NotBlank
        String nome,
        @NotNull
        Integer capacidade) {
}
