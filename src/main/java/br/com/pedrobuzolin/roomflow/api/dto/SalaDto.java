package br.com.pedrobuzolin.roomflow.api.dto;

import br.com.pedrobuzolin.roomflow.api.model.Sala;

public record SalaDto(Long id, String nome, Integer capacidade) {
    public SalaDto(Sala sala) {this(sala.getId(), sala.getNome(), sala.getCapacidade());}
}
