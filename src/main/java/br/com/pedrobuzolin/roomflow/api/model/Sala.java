package br.com.pedrobuzolin.roomflow.api.model;

import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import jakarta.persistence.*;

@Entity
@Table(name="salas")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Integer capacidade;
    private boolean ativa;

    public Sala() {}

    public Sala(String nome, Integer capacidade) {
        validarNome(nome);
        validarCapacidade(capacidade);

        this.nome = nome;
        this.capacidade = capacidade;
        this.ativa = true;
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

    public boolean isAtiva() {
        return ativa;
    }

    public void atualizarDados(String nome, Integer capacidade) {
        validarNome(nome);
        validarCapacidade(capacidade);

        this.nome = nome;
        this.capacidade = capacidade;
    }

    public void ativarSala() {
        this.ativa = true;
    }

    public void desativarSala() {
        this.ativa = false;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ValidacaoException("Nome da sala obrigatório!");
        }
    }

    private void validarCapacidade(Integer capacidade) {
        if (capacidade == null || capacidade <= 0) {
            throw new ValidacaoException("A capacidade deve ser positiva!");
        }
    }
}
