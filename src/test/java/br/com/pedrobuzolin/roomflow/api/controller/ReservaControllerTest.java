package br.com.pedrobuzolin.roomflow.api.controller;

import br.com.pedrobuzolin.roomflow.api.dto.ReservaDto;
import br.com.pedrobuzolin.roomflow.api.dto.SalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.SolicitacaoReservaDto;
import br.com.pedrobuzolin.roomflow.api.dto.UsuarioDto;
import br.com.pedrobuzolin.roomflow.api.exception.RecursoNaoEncontradoException;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.StatusReserva;
import br.com.pedrobuzolin.roomflow.api.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {
    @MockitoBean
    private ReservaService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoListarTodasReservas() throws Exception {
        when(service.listarTodasReservas(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/reservas")
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoListarReservasAtivas() throws Exception {
        when(service.listarReservasAtivas(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/reservas/ativas")
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoBuscarReservaPorId() throws Exception {
        Long id = 1L;
        SalaDto salaDto = new SalaDto(1L, "Sala Azul", 20);
        UsuarioDto usuarioDto = new UsuarioDto(1L, "Teste", "teste@email.com");
        ReservaDto dto = new ReservaDto(id, salaDto, usuarioDto,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"),
                StatusReserva.ATIVA);
        when(service.buscarReservaPorId(id)).thenReturn(dto);

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/reservas/{id}", id)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoBuscarReservaPorIdInexistente() throws Exception {
        Long id = 1L;
        when(service.buscarReservaPorId(id)).thenThrow(new RecursoNaoEncontradoException("Reserva não encontrada"));

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/reservas/{id}", id)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoBuscarReservasPorSala() throws Exception {
        String idSala = "1";
        when(service.buscarReservasPorSala(any(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/reservas/sala")
                        .param("idSala", idSala)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoBuscarReservasPorSalaInexistente() throws Exception {
        String idSala = "1";
        when(service.buscarReservasPorSala(any(), any(Pageable.class))).thenThrow(new RecursoNaoEncontradoException("Sala não encontrada"));

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/reservas/sala")
                        .param("idSala", idSala)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoBuscarReservasPorPeriodo() throws Exception {
        when(service.buscarReservasPorPeriodo(any(), any(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/reservas/periodo")
                        .param("dataInicio", "2026-08-10T10:00:00")
                        .param("dataFim", "2026-08-10T11:00:00")
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaRequisicaoBuscarReservasPorPeriodoInvalido() throws Exception {
        when(service.buscarReservasPorPeriodo(any(), any(), any(Pageable.class)))
                .thenThrow(new ValidacaoException("A data inicio deve ser anterior a data fim"));

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/reservas/periodo")
                        .param("dataInicio", "2026-08-10T12:00:00")
                        .param("dataFim", "2026-08-10T11:00:00")
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo201ParaRequisicaoCriarReserva() throws Exception {
        SolicitacaoReservaDto dto = new SolicitacaoReservaDto(1L, 1L,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));
        when(service.criarReserva(dto)).thenReturn(1L);

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(201, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoCriarReservaComSalaInexistente() throws Exception {
        SolicitacaoReservaDto dto = new SolicitacaoReservaDto(1L, 1L,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));
        when(service.criarReserva(dto)).thenThrow(new RecursoNaoEncontradoException("Sala não encontrada"));

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoCriarReservaComUsuarioInexistente() throws Exception {
        SolicitacaoReservaDto dto = new SolicitacaoReservaDto(1L, 1L,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));
        when(service.criarReserva(dto)).thenThrow(new RecursoNaoEncontradoException("Usuario não encontrado"));

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaRequisicaoCriarReservaComPeriodoInvalido() throws Exception {
        SolicitacaoReservaDto dto = new SolicitacaoReservaDto(1L, 1L,
                LocalDateTime.parse("2026-08-10T12:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));
        when(service.criarReserva(dto)).thenThrow(new ValidacaoException("A data inicio deve ser anterior a data fim"));

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaRequisicaoCriarReservaComDadosInvalidos() throws Exception {
        SolicitacaoReservaDto dtoInvalido = new SolicitacaoReservaDto(null, null,
                LocalDateTime.parse("2026-08-10T10:00:00"),
                LocalDateTime.parse("2026-08-10T11:00:00"));

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido))
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoCancelarReserva() throws Exception {
        Long id = 1L;

        MockHttpServletResponse response = mockMvc.perform(
                patch("/api/v1/reservas/cancelar/{id}", id)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoCancelarReservaInexistente() throws Exception {
        Long id = 1L;

        doThrow(new RecursoNaoEncontradoException("Reserva não encontrada!"))
                .when(service).cancelarReserva(id);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/api/v1/reservas/cancelar/{id}", id)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }
}