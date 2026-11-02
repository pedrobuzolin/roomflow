package br.com.pedrobuzolin.roomflow.api.controller;

import br.com.pedrobuzolin.roomflow.api.dto.AtualizacaoSalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.CadastroSalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.SalaDto;
import br.com.pedrobuzolin.roomflow.api.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/salas")
public class SalaController {
    private final SalaService service;

    public SalaController(SalaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SalaDto>> listarTodasSalasAtivas() {
        List<SalaDto> salas = service.buscarSalasAtivas();
        return ResponseEntity.ok().body(salas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaDto> buscarSalaPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.buscarSalaPorId(id));
    }

    @GetMapping(("/disponiveis"))
    public ResponseEntity<List<SalaDto>> listarTodasSalasDisponiveis(@RequestParam LocalDateTime dataInicial, @RequestParam LocalDateTime dataFinal) {
        List<SalaDto> salas = service.buscarSalasDisponiveis(dataInicial, dataFinal);
        return ResponseEntity.ok().body(salas);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarSala(@RequestBody @Valid CadastroSalaDto dto, UriComponentsBuilder uriBuilder) {
        Long id = service.cadastrarSala(dto);
        URI location = uriBuilder.path("/api/v1/salas/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarSala(@PathVariable Long id, @RequestBody @Valid AtualizacaoSalaDto dto){
        service.atualizarSala(id, dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarSala(@PathVariable Long id){
        service.ativarSala(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Void> desativarSala(@PathVariable Long id){
        service.desativarSala(id);
        return ResponseEntity.ok().build();
    }
}
