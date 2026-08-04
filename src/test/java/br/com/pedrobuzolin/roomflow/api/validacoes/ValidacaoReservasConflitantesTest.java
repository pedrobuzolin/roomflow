package br.com.pedrobuzolin.roomflow.api.validacoes;

import br.com.pedrobuzolin.roomflow.api.dto.SolicitacaoReservaDto;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.StatusReserva;
import br.com.pedrobuzolin.roomflow.api.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidacaoReservasConflitantesTest {

    @Mock
    private ReservaRepository reservaRepository;

    private ValidacaoReservasConflitantes validacao;

    @BeforeEach
    void configurar() {
        validacao = new ValidacaoReservasConflitantes(reservaRepository);
    }

    @Test
    void deveriaLancarExcecaoQuandoExisteReservaConflitante() {
        SolicitacaoReservaDto dto = new SolicitacaoReservaDto(1L, 1L,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));

        when(reservaRepository.buscarReservaConflitantePorSalaEHorario(
                StatusReserva.ATIVA, dto.idSala(), dto.dataInicio(), dto.dataFim()))
                .thenReturn(true);

        assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void naoDeveriaLancarExcecaoQuandoNaoExisteConflito() {
        SolicitacaoReservaDto dto = new SolicitacaoReservaDto(1L, 1L,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));

        when(reservaRepository.buscarReservaConflitantePorSalaEHorario(
                StatusReserva.ATIVA, dto.idSala(), dto.dataInicio(), dto.dataFim()))
                .thenReturn(false);

        assertDoesNotThrow(() -> validacao.validar(dto));
    }
}