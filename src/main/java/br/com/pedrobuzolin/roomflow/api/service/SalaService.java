package br.com.pedrobuzolin.roomflow.api.service;

import br.com.pedrobuzolin.roomflow.api.dto.CadastroSalaDto;
import br.com.pedrobuzolin.roomflow.api.dto.SalaDto;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.Sala;
import br.com.pedrobuzolin.roomflow.api.model.StatusSala;
import br.com.pedrobuzolin.roomflow.api.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaService {
    @Autowired
    private SalaRepository salaRepository;

    public List<SalaDto> buscarSalasDisponiveis() {
        return salaRepository.findAllByStatus(StatusSala.LIVRE)
                .stream()
                .map(SalaDto::new)
                .toList();
    }

    public void cadastrar(CadastroSalaDto dto) {
        boolean jaCadastrado = salaRepository.existsByNome(dto.nome());
        if (jaCadastrado) {
            throw new ValidacaoException("Sala já cadastrada");
        }
        salaRepository.save(new Sala(dto));
    }

    public void reservarSala(SalaDto dto) {
        Sala sala = salaRepository.getReferenceById(dto.id());
        sala.marcarComoReservada();
    }

    public void liberarSala(SalaDto dto) {
        Sala sala = salaRepository.getReferenceById(dto.id());
        sala.marcarComoLivre();
    }

}
