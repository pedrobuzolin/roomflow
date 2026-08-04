package br.com.pedrobuzolin.roomflow.api.service;

import br.com.pedrobuzolin.roomflow.api.dto.CadastroSalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.SalaDto;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.Reserva;
import br.com.pedrobuzolin.roomflow.api.model.Sala;
import br.com.pedrobuzolin.roomflow.api.model.StatusReserva;
import br.com.pedrobuzolin.roomflow.api.model.Usuario;
import br.com.pedrobuzolin.roomflow.api.repository.ReservaRepository;
import br.com.pedrobuzolin.roomflow.api.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private ReservaRepository reservaRepository;

    private SalaService service;

    @BeforeEach
    void configurar() {
        service = new SalaService(salaRepository, reservaRepository);
    }

    @Test
    void deveriaLancarExcecaoQuandoNomeDaSalaJaExiste() {
        CadastroSalaDto dto = new CadastroSalaDto("Sala Azul", 20);
        when(salaRepository.existsByNome("Sala Azul")).thenReturn(true);

        assertThrows(ValidacaoException.class, () -> service.cadastrarSala(dto));
        verify(salaRepository, never()).save(any());
    }

    @Test
    void deveriaLancarExcecaoQuandoDataInicialNaoForAnteriorADataFinal() {
        LocalDateTime inicio = LocalDateTime.parse("2026-08-10T12:00:00");
        LocalDateTime fim = LocalDateTime.parse("2026-08-10T11:00:00");

        assertThrows(ValidacaoException.class, () -> service.buscarSalasDisponiveis(inicio, fim));
    }

    @Test
    void deveriaExcluirSalaComReservaConflitanteDaListaDeDisponiveis() {
        Sala salaDisponivel = new Sala("Sala Verde", 10);
        Sala salaIndisponivel = new Sala("Sala Azul", 20);
        ReflectionTestUtils.setField(salaDisponivel, "id", 1L);
        ReflectionTestUtils.setField(salaIndisponivel, "id", 2L);

        Usuario usuario = new Usuario("Teste", "teste@email.com");
        LocalDateTime inicio = LocalDateTime.parse("2026-08-10T10:00:00");
        LocalDateTime fim = LocalDateTime.parse("2026-08-10T11:00:00");
        Reserva reservaConflitante = new Reserva(salaIndisponivel, usuario, inicio, fim);

        when(reservaRepository.buscarReservasConflitantes(StatusReserva.ATIVA, fim, inicio))
                .thenReturn(List.of(reservaConflitante));
        when(salaRepository.findAllByAtivaTrue())
                .thenReturn(List.of(salaDisponivel, salaIndisponivel));

        List<SalaDto> disponiveis = service.buscarSalasDisponiveis(inicio, fim);

        assertEquals(1, disponiveis.size());
        assertEquals("Sala Verde", disponiveis.get(0).nome());
    }
}