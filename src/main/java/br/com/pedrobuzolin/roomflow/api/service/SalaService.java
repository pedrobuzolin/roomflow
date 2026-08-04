package br.com.pedrobuzolin.roomflow.api.service;

import br.com.pedrobuzolin.roomflow.api.dto.AtualizacaoSalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.CadastroSalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.SalaDto;
import br.com.pedrobuzolin.roomflow.api.exception.RecursoNaoEncontradoException;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.Reserva;
import br.com.pedrobuzolin.roomflow.api.model.Sala;
import br.com.pedrobuzolin.roomflow.api.model.StatusReserva;
import br.com.pedrobuzolin.roomflow.api.repository.ReservaRepository;
import br.com.pedrobuzolin.roomflow.api.repository.SalaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalaService {
    private final SalaRepository salaRepository;
    private final ReservaRepository reservaRepository;

    public SalaService(SalaRepository salaRepository, ReservaRepository reservaRepository) {
        this.salaRepository = salaRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<SalaDto> buscarSalasAtivas() {
        return salaRepository.findAllByAtivaTrue()
                .stream()
                .map(SalaDto::new)
                .toList();
    }

    public List<SalaDto> buscarSalasDisponiveis(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        if (!dataInicial.isBefore(dataFinal)) {
            throw new ValidacaoException("A data inicial deve ser anterior à data final!");
        }

        List<Reserva> conflitantes = reservaRepository.buscarReservasConflitantes(
                StatusReserva.ATIVA, dataFinal, dataInicial);

        Set<Long> salasIndisponiveis = conflitantes
                .stream()
                .map(reserva -> reserva.getSala().getId())
                .collect(Collectors.toSet());

        return salaRepository.findAllByAtivaTrue()
                .stream()
                .filter(sala -> !salasIndisponiveis.contains(sala.getId()))
                .map(SalaDto::new)
                .toList();
    }

    public SalaDto buscarSalaPorId(Long id) {
        return salaRepository.findById(id)
                .map(SalaDto::new)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala não encontrada"));
    }

    public Long cadastrarSala(CadastroSalaDto dto) {
        boolean jaCadastrado = salaRepository.existsByNome(dto.nome());
        if (jaCadastrado) {
            throw new ValidacaoException("Sala já cadastrada");
        }
        Sala sala = salaRepository.save(new Sala(dto.nome(), dto.capacidade()));
        return sala.getId();
    }

    public void atualizarSala(Long id, AtualizacaoSalaDto dto) {
        Sala sala = verificarExistenciaSalaPorId(id);
        sala.atualizarDados(dto.nome(), dto.capacidade());
    }

    public void ativarSala(Long id) {
        Sala sala = verificarExistenciaSalaPorId(id);
        sala.ativarSala();
    }

    public void desativarSala(Long id) {
        Sala sala = verificarExistenciaSalaPorId(id);
        sala.desativarSala();
    }

    private Sala verificarExistenciaSalaPorId(Long id) {
        return salaRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Sala não encontrada"));
    }
}
