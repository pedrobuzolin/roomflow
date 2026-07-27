package br.com.pedrobuzolin.roomflow.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AtualizacaoUsuarioDto(
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email) {
}
