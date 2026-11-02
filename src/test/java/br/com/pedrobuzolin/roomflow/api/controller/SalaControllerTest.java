package br.com.pedrobuzolin.roomflow.api.controller;

import br.com.pedrobuzolin.roomflow.api.dto.AtualizacaoSalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.CadastroSalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.SalaDto;
import br.com.pedrobuzolin.roomflow.api.exception.RecursoNaoEncontradoException;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.service.SalaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(SalaController.class)
class SalaControllerTest {
    @MockitoBean
    private SalaService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoListarTodasSalas() throws Exception {
        when(service.buscarSalasAtivas()).thenReturn(List.of());

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/salas")
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoBuscarSalaPorId() throws Exception {
        SalaDto salaDto = new SalaDto(1L, "Sala Azul", 20);
        when(service.buscarSalaPorId(1L)).thenReturn(salaDto);

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/salas/{id}", 1)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoBuscarSalaPorIdInexistente() throws Exception {
        Long id = 1L;
        when(service.buscarSalaPorId(id)).thenThrow(new RecursoNaoEncontradoException("Sala não encontrada!"));

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/salas/{id}", id)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());

    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoListarTodasSalasDisponiveis() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SalaDto> paginaVazia = new PageImpl<>(List.of(), pageable, 0);

        when(service.buscarSalasDisponiveis(
                any(), any(), any(Pageable.class)
        )).thenReturn(paginaVazia);

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/salas/disponiveis")
                        .param("dataInicial", "2026-08-10T10:00:00")
                        .param("dataFinal", "2026-08-10T11:00:00")
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaRequisicaoListarTodasSalasDisponiveisComDatasInvalidas() throws Exception {
        when(service.buscarSalasDisponiveis(
                any(), any(), any(Pageable.class)
        )).thenThrow(new ValidacaoException(
                "A data inicial deve ser anterior à data final!"
        ));

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/salas/disponiveis")
                        .param("dataInicial", "2026-08-10T12:00:00")
                        .param("dataFinal", "2026-08-10T11:00:00")
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo201ParaRequisicaoCadastrarSala() throws Exception {
        CadastroSalaDto dto = new CadastroSalaDto("Sala Azul", 20);
        when(service.cadastrarSala(dto)).thenReturn(1L);

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(201, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaRequisicaoCadastrarSalaComDadosInvalidos() throws Exception {
        CadastroSalaDto dtoInvalido = new CadastroSalaDto("", -10);

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido))
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaRequisicaoCadastrarSalaExistente() throws Exception {
        CadastroSalaDto dto = new CadastroSalaDto("Sala Azul", 20);
        when(service.cadastrarSala(dto)).thenThrow(new ValidacaoException("Sala já cadastrada"));

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoAtualizarSala() throws Exception {
        Long id = 1L;
        AtualizacaoSalaDto dto = new AtualizacaoSalaDto("Sala Azul", 30);

        MockHttpServletResponse response = mockMvc.perform(
                put("/api/v1/salas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaRequisicaoAtualizarSalaComDadosInvalidos() throws Exception {
        Long id = 1L;
        AtualizacaoSalaDto dto = new AtualizacaoSalaDto("", -30);

        MockHttpServletResponse response = mockMvc.perform(
                put("/api/v1/salas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoAtualizarSalaInexistente() throws Exception {
        Long id = 1L;
        AtualizacaoSalaDto dto = new AtualizacaoSalaDto("Sala Azul", 40);
        doThrow(new RecursoNaoEncontradoException("Sala não encontrada!"))
                .when(service).atualizarSala(id, dto);

        MockHttpServletResponse response = mockMvc.perform(
                put("/api/v1/salas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoDesativarSala() throws Exception {
        Long id = 1L;

        MockHttpServletResponse response = mockMvc.perform(
                patch("/api/v1/salas/desativar/{id}", id)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoDesativarSalaInexistente() throws Exception {
        Long id = 1L;
        doThrow(new RecursoNaoEncontradoException("Sala não encontrada!"))
                .when(service).desativarSala(id);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/api/v1/salas/desativar/{id}", id)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoAtivarSala() throws Exception {
        Long id = 1L;

        MockHttpServletResponse response = mockMvc.perform(
                patch("/api/v1/salas/ativar/{id}", id)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoAtivarSalaInexistente() throws Exception {
        Long id = 1L;
        doThrow(new RecursoNaoEncontradoException("Sala não encontrada!"))
                .when(service).ativarSala(id);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/api/v1/salas/ativar/{id}", id)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }
}