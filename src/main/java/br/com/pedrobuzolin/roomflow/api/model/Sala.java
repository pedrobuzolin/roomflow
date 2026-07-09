package br.com.pedrobuzolin.roomflow.api.model;

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
}
