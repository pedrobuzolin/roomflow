package br.com.pedrobuzolin.roomflow.api.service;

import br.com.pedrobuzolin.roomflow.api.dto.CadastroUsuarioDto;
import br.com.pedrobuzolin.roomflow.api.exception.RecursoNaoEncontradoException;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.Usuario;
import br.com.pedrobuzolin.roomflow.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    private UsuarioService service;

    @BeforeEach
    void configurar() {
        service = new UsuarioService(repository);
    }

    @Test
    void deveriaLancarExcecaoQuandoEmailJaCadastrado() {
        CadastroUsuarioDto dto = new CadastroUsuarioDto("Teste", "teste@email.com");
        when(repository.existsByEmail("teste@email.com")).thenReturn(true);

        assertThrows(ValidacaoException.class, () -> service.cadastrar(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void deveriaLancarExcecaoQuandoUsuarioNaoEstaAtivo() {
        when(repository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarUsuarioPorId(1L));
    }

    @Test
    void deveriaAnonimizarUsuarioAoDeletar() {
        Usuario usuario = new Usuario("Teste", "teste@email.com");
        when(repository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(usuario));

        service.deletar(1L);

        assertFalse(usuario.isAtivo());
        assertEquals("Usuario Removido", usuario.getNome());
    }
}