package br.com.pedrobuzolin.roomflow.api.dto;

import br.com.pedrobuzolin.roomflow.api.model.Usuario;

public record UsuarioDto(Long id, String nome, String email) {
    public UsuarioDto(Usuario usuario) {this(usuario.getId(), usuario.getNome(), usuario.getEmail());}
}
