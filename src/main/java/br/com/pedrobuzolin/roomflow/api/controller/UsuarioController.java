package br.com.pedrobuzolin.roomflow.api.controller;

import br.com.pedrobuzolin.roomflow.api.dto.AtualizacaoUsuarioDto;
import br.com.pedrobuzolin.roomflow.api.dto.CadastroUsuarioDto;
import br.com.pedrobuzolin.roomflow.api.dto.UsuarioDto;
import br.com.pedrobuzolin.roomflow.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listarTodosUsuarios() {
        List<UsuarioDto> usuarios = service.buscarTodosUsuarios();
        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.buscarUsuarioPorId(id));
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarUsuario(@RequestBody @Valid CadastroUsuarioDto dto, UriComponentsBuilder uriBuilder) {
        Long id = service.cadastrar(dto);
        URI location = uriBuilder.path("/api/v1/usuarios/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid AtualizacaoUsuarioDto dto) {
        service.atualizar(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
