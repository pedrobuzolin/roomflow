package br.com.pedrobuzolin.roomflow.api.model;

import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    private final Sala sala = new Sala("Sala Azul", 20);
    private final Usuario usuario = new Usuario("Teste", "teste@email.com");
    private final LocalDateTime dataInicio = LocalDateTime.now().plusDays(1);
    private final LocalDateTime dataFim = dataInicio.plusHours(1);

    @Test
    void deveriaCriarReservaAtivaComDadosValidos() {
        Reserva reserva = new Reserva(sala, usuario, dataInicio, dataFim);

        assertEquals(StatusReserva.ATIVA, reserva.getStatusReserva());
    }

    @Test
    void deveriaLancarExcecaoQuandoInicioForDepoisDoFim() {
        LocalDateTime fim = dataFim.minusHours(2);

        assertThrows(ValidacaoException.class, () -> new Reserva(sala, usuario, dataInicio, fim));
    }

    @Test
    void deveriaLancarExcecaoQuandoInicioForIgualAoFim() {
        assertThrows(ValidacaoException.class, () -> new Reserva(sala, usuario, dataInicio, dataInicio));
    }

    @Test
    void deveriaMarcarReservaComoCanceladaComReservaFutura() {
        Reserva reserva = new Reserva(sala, usuario, dataInicio, dataFim);

        reserva.marcarReservaComoCancelada();

        assertEquals(StatusReserva.CANCELADA, reserva.getStatusReserva());
    }

    @Test
    void deveriaLancarExececaoQuandoMarcarReservaComoCanceladaComReservaEmAndamento() {
        Reserva reserva = new Reserva(sala, usuario, dataInicio.minusMinutes(30), dataFim.plusMinutes(30));

        reserva.marcarReservaComoCancelada();

        assertThrows(ValidacaoException.class, reserva::marcarReservaComoCancelada);
    }

    @Test
    void deveriaLancarExececaoQuandoMarcarReservaComoCanceladaComReservaJaEncerrada() {
        Reserva reserva = new Reserva(sala, usuario, dataInicio.minusHours(2), dataFim.minusHours(1));

        reserva.marcarReservaComoCancelada();

        assertThrows(ValidacaoException.class, reserva::marcarReservaComoCancelada);
    }

    @Test
    void deveriaLancarExececaoQuandoMarcarReservaComoCanceladaComReservaJaCancelada() {
        Reserva reserva = new Reserva(sala, usuario, dataInicio, dataFim);

        reserva.marcarReservaComoCancelada();

        assertThrows(ValidacaoException.class, reserva::marcarReservaComoCancelada);
    }
}