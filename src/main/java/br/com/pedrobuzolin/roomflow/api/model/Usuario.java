package br.com.pedrobuzolin.roomflow.api.model;

import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import jakarta.persistence.*;

import java.util.regex.Pattern;

@Entity
@Table(name="usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");


    public Usuario() {}

    public Usuario(String nome, String email) {
        validarNome(nome);
        validarEmail(email);

        this.nome = nome;
        this.email = email;
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

    public void atualizarDados(String nome, String email) {
        validarNome(nome);
        validarEmail(email);

        this.nome = nome;
        this.email = email;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ValidacaoException("Nome de usuário obrigatório!");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidacaoException("E-mail do usuário obrigatório!");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidacaoException("E-mail inválido!");
        }
    }
}
