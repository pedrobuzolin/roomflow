package br.com.pedrobuzolin.roomflow.api.service;

import br.com.pedrobuzolin.roomflow.api.dto.CadastroSalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.SalaDto;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.Sala;
import br.com.pedrobuzolin.roomflow.api.model.StatusReserva;
import br.com.pedrobuzolin.roomflow.api.repository.ReservaRepository;
import br.com.pedrobuzolin.roomflow.api.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(
                ValidacaoException.class,
                () -> service.buscarSalasDisponiveis(inicio, fim, pageable)
        );

        verifyNoInteractions(salaRepository);
    }

    @Test
    void deveriaRetornarSomenteSalasDisponiveis() {
        Sala salaDisponivel = new Sala("Sala Verde", 10);

        LocalDateTime inicio = LocalDateTime.parse("2026-11-10T10:00:00");
        LocalDateTime fim = LocalDateTime.parse("2026-11-10T11:00:00");

        Pageable pageable = PageRequest.of(0, 10);

        Page<Sala> paginaSalas = new PageImpl<>(
                List.of(salaDisponivel),
                pageable,
                1
        );

        when(salaRepository.buscarSalasDisponiveis(
                inicio, fim, StatusReserva.ATIVA, pageable
        )).thenReturn(paginaSalas);

        Page<SalaDto> resultado =
                service.buscarSalasDisponiveis(inicio, fim, pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(1, resultado.getTotalPages());
        assertEquals("Sala Verde", resultado.getContent().get(0).nome());

        verify(salaRepository).buscarSalasDisponiveis(
                inicio, fim, StatusReserva.ATIVA, pageable
        );

        verifyNoInteractions(reservaRepository);
    }
}