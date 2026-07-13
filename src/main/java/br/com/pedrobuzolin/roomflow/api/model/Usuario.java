package br.com.pedrobuzolin.roomflow.api.model;

import br.com.pedrobuzolin.roomflow.api.dto.AtualizacaoUsuarioDto;
import br.com.pedrobuzolin.roomflow.api.dto.CadastroUsuarioDto;
import jakarta.persistence.*;

@Entity
@Table(name="usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;

    public Usuario() {}

    public Usuario(CadastroUsuarioDto dto) {
        this.nome = dto.nome();
        this.email = dto.email();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void atualizarDados(AtualizacaoUsuarioDto dto) {
        this.nome = dto.nome();
        this.email = dto.email();
    }
}
