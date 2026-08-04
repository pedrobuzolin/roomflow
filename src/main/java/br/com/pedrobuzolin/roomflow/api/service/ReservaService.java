package br.com.pedrobuzolin.roomflow.api.service;

import br.com.pedrobuzolin.roomflow.api.dto.ReservaDto;
import br.com.pedrobuzolin.roomflow.api.dto.SolicitacaoReservaDto;
import br.com.pedrobuzolin.roomflow.api.exception.RecursoNaoEncontradoException;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.Reserva;
import br.com.pedrobuzolin.roomflow.api.model.Sala;
import br.com.pedrobuzolin.roomflow.api.model.StatusReserva;
import br.com.pedrobuzolin.roomflow.api.model.Usuario;
import br.com.pedrobuzolin.roomflow.api.repository.ReservaRepository;
import br.com.pedrobuzolin.roomflow.api.repository.SalaRepository;
import br.com.pedrobuzolin.roomflow.api.repository.UsuarioRepository;
import br.com.pedrobuzolin.roomflow.api.validacoes.ValidacaoSolicitacaoReserva;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservaService {
    private final ReservaRepository repository;
    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;
    private final List<ValidacaoSolicitacaoReserva> validacoes;

    public ReservaService(ReservaRepository repository, SalaRepository salaRepository,
                          UsuarioRepository usuarioRepository, List<ValidacaoSolicitacaoReserva> validacoes) {
        this.repository = repository;
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
        this.validacoes = validacoes;
    }

    public Long criarReserva(SolicitacaoReservaDto dto) {
        Sala sala = salaRepository.findById(dto.idSala()).orElseThrow(() -> new RecursoNaoEncontradoException("Sala não encontrada"));
        if(!sala.isAtiva()) {
            throw new ValidacaoException("Sala inativa!");
        }
        Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(dto.idUsuario()).orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado"));

        validacoes.forEach(v -> v.validar(dto));

        Reserva reserva = new Reserva(sala, usuario, dto.dataInicio(), dto.dataFim());
        repository.save(reserva);

        return reserva.getId();
    }

    public void cancelarReserva(Long id) {
        Reserva reserva = buscarReserva(id);
        reserva.marcarReservaComoCancelada();
        repository.save(reserva);
    }

    public Page<ReservaDto> listarTodasReservas(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ReservaDto::new);
    }

    public Page<ReservaDto> listarReservasAtivas(Pageable pageable) {
        return repository.findByStatusReserva(StatusReserva.ATIVA, pageable)
                .map(ReservaDto::new);
    }

    public ReservaDto buscarReservaPorId (Long id) {
        return repository.findById(id)
                .map(ReservaDto::new)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada!"));
    }

    public Page<ReservaDto> buscarReservasPorSala(Long idSala, Pageable pageable) {
        Sala sala = salaRepository.findById(idSala).orElseThrow(() -> new RecursoNaoEncontradoException("Sala não encontrada"));
        return repository.findBySala(sala, pageable)
                .map(ReservaDto::new);
    }

    public Page<ReservaDto> buscarReservasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable) {
        if (!dataInicio.isBefore(dataFim)) {
            throw new ValidacaoException("A data inicial deve ser anterior à data final!");
        }
        return repository.buscarPorPeriodo(dataInicio, dataFim, pageable)
                .map(ReservaDto::new);
    }

    private Reserva buscarReserva(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada!"));
    }
}
