package br.com.pedrobuzolin.roomflow.api.service;

import br.com.pedrobuzolin.roomflow.api.dto.AtualizacaoUsuarioDto;
import br.com.pedrobuzolin.roomflow.api.dto.CadastroUsuarioDto;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.Usuario;
import br.com.pedrobuzolin.roomflow.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public void cadastrar(CadastroUsuarioDto dto) {
        boolean jaCadastrado = repository.existsByNomeOrEmail(dto.nome(), dto.email());

        if (jaCadastrado) {
            throw new ValidacaoException("Usuário já cadastrado!");
        }

        repository.save(new Usuario(dto));
    }

    public void atualizar(AtualizacaoUsuarioDto dto) {
        Usuario usuario = repository.getReferenceById(dto.id());
        usuario.atualizarDados(dto);
    }
}
