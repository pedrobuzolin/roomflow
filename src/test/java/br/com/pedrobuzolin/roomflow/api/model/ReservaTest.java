package br.com.pedrobuzolin.roomflow.api.model;

import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    private final Sala sala = new Sala("Sala Azul", 20);
    private final Usuario usuario = new Usuario("Teste", "teste@email.com");

    @Test
    void deveriaCriarReservaAtivaComDadosValidos() {
        Reserva reserva = new Reserva(sala, usuario,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));

        assertEquals(StatusReserva.ATIVA, reserva.getStatusReserva());
    }

    @Test
    void deveriaLancarExcecaoQuandoInicioForDepoisDoFim() {
        LocalDateTime inicio = LocalDateTime.parse("2026-08-10T12:00:00");
        LocalDateTime fim = LocalDateTime.parse("2026-08-10T11:00:00");

        assertThrows(ValidacaoException.class, () -> new Reserva(sala, usuario, inicio, fim));
    }

    @Test
    void deveriaLancarExcecaoQuandoInicioForIgualAoFim() {
        LocalDateTime horario = LocalDateTime.parse("2026-08-10T10:00:00");

        assertThrows(ValidacaoException.class, () -> new Reserva(sala, usuario, horario, horario));
    }

    @Test
    void deveriaMarcarReservaComoCancelada() {
        Reserva reserva = new Reserva(sala, usuario,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));

        reserva.marcarReservaComoCancelada();

        assertEquals(StatusReserva.CANCELADA, reserva.getStatusReserva());
    }
}