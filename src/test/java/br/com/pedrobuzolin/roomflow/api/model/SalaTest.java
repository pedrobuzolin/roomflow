package br.com.pedrobuzolin.roomflow.api.model;

import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalaTest {

    @Test
    void deveriaCriarSalaAtivaComDadosValidos() {
        Sala sala = new Sala("Sala Azul", 20);

        assertEquals("Sala Azul", sala.getNome());
        assertEquals(20, sala.getCapacidade());
        assertTrue(sala.isAtiva());
    }

    @Test
    void deveriaLancarExcecaoQuandoNomeForVazio() {
        assertThrows(ValidacaoException.class, () -> new Sala("", 20));
    }

    @Test
    void deveriaLancarExcecaoQuandoCapacidadeForNula() {
        assertThrows(ValidacaoException.class, () -> new Sala("Sala Azul", null));
    }

    @Test
    void deveriaLancarExcecaoQuandoCapacidadeForZero() {
        assertThrows(ValidacaoException.class, () -> new Sala("Sala Azul", 0));
    }

    @Test
    void deveriaLancarExcecaoQuandoCapacidadeForNegativa() {
        assertThrows(ValidacaoException.class, () -> new Sala("Sala Azul", -5));
    }

    @Test
    void deveriaAtualizarDadosComValoresValidos() {
        Sala sala = new Sala("Sala Azul", 20);

        sala.atualizarDados("Sala Verde", 30);

        assertEquals("Sala Verde", sala.getNome());
        assertEquals(30, sala.getCapacidade());
    }

    @Test
    void deveriaDesativarSala() {
        Sala sala = new Sala("Sala Azul", 20);

        sala.desativarSala();
        assertFalse(sala.isAtiva());
    }

    @Test
    void deveriaAtivartivarSala() {
        Sala sala = new Sala("Sala Azul", 20);

        sala.ativarSala();
        assertTrue(sala.isAtiva());
    }
}