package br.com.pedrobuzolin.roomflow.api.model;

import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void deveriaCriarUsuarioComDadosValidos() {
        Usuario usuario = new Usuario("Teste", "teste@email.com");

        assertEquals("Teste", usuario.getNome());
        assertEquals("teste@email.com", usuario.getEmail());
        assertTrue(usuario.isAtivo());
    }

    @Test
    void deveriaLancarExcecaoQuandoNomeForVazio() {
        assertThrows(ValidacaoException.class, () -> new Usuario("", "teste@email.com"));
    }

    @Test
    void deveriaLancarExcecaoQuandoEmailForVazio() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Teste", ""));
    }

    @Test
    void deveriaLancarExcecaoQuandoEmailNaoTiverArroba() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Teste", "email-invalido"));
    }

    @Test
    void deveriaAceitarEmailComDoisNiveisDeDominio() {
        assertDoesNotThrow(() -> new Usuario("Teste", "teste@email.com.br"));
    }

    @Test
    void deveriaAtualizarDadosComValoresValidos() {
        Usuario usuario = new Usuario("Teste", "teste@email.com");

        usuario.atualizarDados("Teste teste", "teste.teste@email.com");

        assertEquals("Teste teste", usuario.getNome());
        assertEquals("teste.teste@email.com", usuario.getEmail());
    }

    @Test
    void deveriaAnonimizarUsuario() {
        Usuario usuario = new Usuario("Teste", "teste@email.com");

        usuario.anonimizar();

        assertEquals("Usuario Removido", usuario.getNome());
        assertFalse(usuario.isAtivo());
        assertTrue(usuario.getEmail().contains("@email.local"));
    }
}