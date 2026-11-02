package br.com.pedrobuzolin.roomflow.api.service;

import br.com.pedrobuzolin.roomflow.api.dto.AtualizacaoUsuarioDto;
import br.com.pedrobuzolin.roomflow.api.dto.CadastroUsuarioDto;
import br.com.pedrobuzolin.roomflow.api.dto.UsuarioDto;
import br.com.pedrobuzolin.roomflow.api.exception.RecursoNaoEncontradoException;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.Usuario;
import br.com.pedrobuzolin.roomflow.api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<UsuarioDto> buscarTodosUsuarios() {
        return repository.findAllByAtivoTrue(Sort.by(Sort.Direction.ASC, "nome"))
                .stream()
                .map(UsuarioDto::new)
                .toList();
    }

    public UsuarioDto buscarUsuarioPorId(Long id) {
        return new UsuarioDto(buscarUsuarioAtivoPorId(id));
    }

    public Long cadastrar(CadastroUsuarioDto dto) {
        boolean jaCadastrado = repository.existsByEmail(dto.email());

        if (jaCadastrado) {
            throw new ValidacaoException("Usuário já cadastrado!");
        }

        Usuario usuario = repository.save(new Usuario(dto.nome(), dto.email()));
        return  usuario.getId();
    }

    public void atualizar(Long id, AtualizacaoUsuarioDto dto) {
        Usuario usuario = buscarUsuarioAtivoPorId(id);

        boolean emailEmUso = repository.existsByEmailAndIdNot(dto.email(), id);
        if (emailEmUso) {
            throw new ValidacaoException("Email já cadastrado!");
        }

        usuario.atualizarDados(dto.nome(), dto.email());
    }

    public void deletar(Long id) {
        Usuario usuario = buscarUsuarioAtivoPorId(id);
        usuario.anonimizar();
    }

    private Usuario buscarUsuarioAtivoPorId(Long id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado!"));
    }
}
