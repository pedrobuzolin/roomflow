package br.com.pedrobuzolin.roomflow.api.dto;

import br.com.pedrobuzolin.roomflow.api.model.Sala;
import br.com.pedrobuzolin.roomflow.api.model.StatusSala;

public record SalaDto(Long id, String nome, Integer capacidade, StatusSala status) {
    public SalaDto(Sala sala) {this(sala.getId(), sala.getNome(), sala.getCapacidade(), sala.getStatus());}
}
