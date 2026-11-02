package br.com.pedrobuzolin.roomflow.api.controller;

import br.com.pedrobuzolin.roomflow.api.dto.ReservaDto;
import br.com.pedrobuzolin.roomflow.api.dto.SolicitacaoReservaDto;
import br.com.pedrobuzolin.roomflow.api.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {
    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ReservaDto>> listarTodasReservas (@PageableDefault(size = 20, sort = "dataInicio") Pageable pageable) {
        Page<ReservaDto> reservas = service.listarTodasReservas(pageable);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/ativas")
    public ResponseEntity<Page<ReservaDto>> listarReservasAtivas (@PageableDefault(size = 20, sort = "dataInicio") Pageable pageable) {
        Page<ReservaDto> reservasAtivas = service.listarReservasAtivas(pageable);
        return ResponseEntity.ok(reservasAtivas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDto> buscarReservaPorId (@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarReservaPorId(id));
    }

    @GetMapping("/sala")
    public ResponseEntity<Page<ReservaDto>> buscarReservasPorSala (@RequestParam("idSala") Long idSala,
                                                                   @PageableDefault(size = 20, sort = "dataInicio") Pageable pageable) {
        Page<ReservaDto> reservasPorSala = service.buscarReservasPorSala(idSala, pageable);
        return ResponseEntity.ok(reservasPorSala);
    }

    @GetMapping("/periodo")
    public ResponseEntity<Page<ReservaDto>> buscarReservasPorPeriodo (
            @RequestParam("dataInicio") LocalDateTime dataInicio, @RequestParam("dataFim") LocalDateTime dataFim,
            @PageableDefault(size = 20, sort = "dataInicio") Pageable pageable) {
        Page<ReservaDto> reservasPorPeriodo = service.buscarReservasPorPeriodo(dataInicio, dataFim, pageable);
        return ResponseEntity.ok(reservasPorPeriodo);
    }

    @PostMapping
    public ResponseEntity<Void> criarReserva(@RequestBody @Valid SolicitacaoReservaDto dto, UriComponentsBuilder uriBuilder) {
        Long id = service.criarReserva(dto);
        URI location = uriBuilder.path("/api/v1/reservas/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/cancelar/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        service.cancelarReserva(id);
        return ResponseEntity.ok().build();
    }
}
