package br.com.pedrobuzolin.roomflow.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoUsuarioDto(
        @NotNull
        Long id,
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email) {
}
