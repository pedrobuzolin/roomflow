package br.com.pedrobuzolin.roomflow.api.controller;

import br.com.pedrobuzolin.roomflow.api.dto.AtualizacaoUsuarioDto;
import br.com.pedrobuzolin.roomflow.api.dto.CadastroUsuarioDto;
import br.com.pedrobuzolin.roomflow.api.dto.UsuarioDto;
import br.com.pedrobuzolin.roomflow.api.exception.RecursoNaoEncontradoException;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {
    @MockitoBean
    private UsuarioService  usuarioService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoListarTodosUsuarios() throws Exception {
        when(usuarioService.buscarTodosUsuarios()).thenReturn(List.of());

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/usuarios")
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoBuscarUsuarioPorId() throws Exception {
        UsuarioDto usuarioDto = new UsuarioDto(1L, "Teste", "teste@email.com");
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(usuarioDto);

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/usuarios/{id}", 1)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());

    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoBuscarUsuarioPorIdInexistente() throws Exception {
        Long id = 1L;
        when(usuarioService.buscarUsuarioPorId(id)).thenThrow(new RecursoNaoEncontradoException("Usuario não encontrado!"));

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/v1/usuarios/{id}", id)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());

    }

    @Test
    void deveriaDevolverCodigo201ParaRequisicaoCadastrarUsuario() throws Exception {
        CadastroUsuarioDto dto = new CadastroUsuarioDto("Teste", "teste@email.com");
        when(usuarioService.cadastrar(dto)).thenReturn(1L);

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(201, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaRequisicaoCadastrarUsuarioComDadosInvalidos() throws Exception {
        CadastroUsuarioDto dtoInvalido = new CadastroUsuarioDto("", "teste-email");

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido))
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaRequisicaoCadastrarUsuarioExistente() throws Exception {
        CadastroUsuarioDto dto = new CadastroUsuarioDto("Teste", "teste@email.com");
        when(usuarioService.cadastrar(dto)).thenThrow(new ValidacaoException("Usuário já cadastrado!"));

        MockHttpServletResponse response = mockMvc.perform(
                post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaRequisicaoAtualizarUsuario() throws Exception {
        Long id = 1L;
        AtualizacaoUsuarioDto dto = new AtualizacaoUsuarioDto("Teste", "teste@email.com");

        MockHttpServletResponse response = mockMvc.perform(
                put("/api/v1/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaRequisicaoAtualizarUsuarioComDadosInvalidos() throws Exception {
        Long id = 1L;
        AtualizacaoUsuarioDto dto = new AtualizacaoUsuarioDto("", "teste-email");

        MockHttpServletResponse response = mockMvc.perform(
                put("/api/v1/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoAtualizarUsuarioInexistente() throws Exception {
        Long id = 1L;
        AtualizacaoUsuarioDto dto = new AtualizacaoUsuarioDto("Teste", "teste@email.com");
        doThrow(new RecursoNaoEncontradoException("Usuário não encontrado!"))
                .when(usuarioService).atualizar(id, dto);

        MockHttpServletResponse response = mockMvc.perform(
                put("/api/v1/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo204ParaRequisicaoDeletarUsuario() throws Exception {
        Long id = 1L;

        MockHttpServletResponse response = mockMvc.perform(
                delete("/api/v1/usuarios/{id}", id)
        ).andReturn().getResponse();

        assertEquals(204, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRequisicaoDeletarUsuarioInexistente() throws Exception {
        Long id = 1L;
        doThrow(new RecursoNaoEncontradoException("Usuário não encontrado!"))
                .when(usuarioService).deletar(id);

        MockHttpServletResponse response = mockMvc.perform(
                delete("/api/v1/usuarios/{id}", id)
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }
}