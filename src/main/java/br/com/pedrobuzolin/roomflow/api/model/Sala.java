package br.com.pedrobuzolin.roomflow.api.model;

import br.com.pedrobuzolin.roomflow.api.dto.CadastroSalaDto;
import jakarta.persistence.*;

@Entity
@Table(name="salas")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Integer capacidade;
    @Enumerated(EnumType.STRING)
    private StatusSala status;

    public Sala() {}

    public Sala(CadastroSalaDto dto) {
        this.nome = dto.nome();
        this.capacidade = dto.capacidade();
        this.status = StatusSala.LIVRE;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public StatusSala getStatus() {
        return status;
    }

    public void marcarComoReservada() {
        this.status = StatusSala.RESERVADA;
    }

    public void marcarComoLivre() {
        this.status = StatusSala.LIVRE;
    }
}
