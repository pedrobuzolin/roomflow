package br.com.pedrobuzolin.roomflow.api.service;

import br.com.pedrobuzolin.roomflow.api.dto.AtualizacaoSalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.CadastroSalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.SalaDto;
import br.com.pedrobuzolin.roomflow.api.exception.RecursoNaoEncontradoException;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.Sala;
import br.com.pedrobuzolin.roomflow.api.model.StatusReserva;
import br.com.pedrobuzolin.roomflow.api.repository.ReservaRepository;
import br.com.pedrobuzolin.roomflow.api.repository.SalaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public Page<SalaDto> buscarSalasDisponiveis(
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable) {

        if (dataInicio == null || dataFim == null ||
                !dataInicio.isBefore(dataFim)) {
            throw new ValidacaoException(
                    "O período informado é inválido!"
            );
        }

        return salaRepository.buscarSalasDisponiveis(
                dataInicio,
                dataFim,
                StatusReserva.ATIVA,
                pageable
        ).map(SalaDto::new);
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
        boolean nomeDeSalaEmUso = salaRepository.existsByNomeAndIdNot(dto.nome(), id);
        if (nomeDeSalaEmUso) {
            throw new ValidacaoException("Nome de sala já existente!");
        }
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
