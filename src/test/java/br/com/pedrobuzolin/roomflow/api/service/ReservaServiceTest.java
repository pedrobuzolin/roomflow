package br.com.pedrobuzolin.roomflow.api.service;

import br.com.pedrobuzolin.roomflow.api.dto.SolicitacaoReservaDto;
import br.com.pedrobuzolin.roomflow.api.exception.RecursoNaoEncontradoException;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.Sala;
import br.com.pedrobuzolin.roomflow.api.model.Usuario;
import br.com.pedrobuzolin.roomflow.api.repository.ReservaRepository;
import br.com.pedrobuzolin.roomflow.api.repository.SalaRepository;
import br.com.pedrobuzolin.roomflow.api.repository.UsuarioRepository;
import br.com.pedrobuzolin.roomflow.api.validacoes.ValidacaoSolicitacaoReserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock private ReservaRepository reservaRepository;
    @Mock private SalaRepository salaRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ValidacaoSolicitacaoReserva validacaoFake;

    private ReservaService service;

    @BeforeEach
    void configurar() {
        service = new ReservaService(reservaRepository, salaRepository, usuarioRepository, List.of(validacaoFake));
    }

    @Test
    void deveriaSalvarReservaQuandoSalaEUsuarioExistem() {
        Sala sala = new Sala("Sala Azul", 20);
        Usuario usuario = new Usuario("Maria Silva", "maria@email.com");
        SolicitacaoReservaDto dto = new SolicitacaoReservaDto(1L, 1L,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));

        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(usuarioRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(usuario));

        service.criarReserva(dto);

        verify(reservaRepository).save(any());
    }

    @Test
    void deveriaLancarExcecaoQuandoSalaNaoExiste() {
        SolicitacaoReservaDto dto = new SolicitacaoReservaDto(1L, 1L,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));

        when(salaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.criarReserva(dto));
    }

    @Test
    void deveriaLancarExcecaoQuandoSalaEstaInativa() {
        Sala salaInativa = new Sala("Sala Azul", 20);
        salaInativa.desativarSala();
        SolicitacaoReservaDto dto = new SolicitacaoReservaDto(1L, 1L,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));

        when(salaRepository.findById(1L)).thenReturn(Optional.of(salaInativa));

        assertThrows(ValidacaoException.class, () -> service.criarReserva(dto));
    }

    @Test
    void naoDeveriaSalvarReservaQuandoValidacaoFalha() {
        Sala sala = new Sala("Sala Azul", 20);
        Usuario usuario = new Usuario("Maria Silva", "maria@email.com");
        SolicitacaoReservaDto dto = new SolicitacaoReservaDto(1L, 1L,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));

        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(usuarioRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(usuario));
        doThrow(new ValidacaoException("Sala já reservada para este horário"))
                .when(validacaoFake).validar(dto);

        assertThrows(ValidacaoException.class, () -> service.criarReserva(dto));
        verify(reservaRepository, never()).save(any());
    }
}