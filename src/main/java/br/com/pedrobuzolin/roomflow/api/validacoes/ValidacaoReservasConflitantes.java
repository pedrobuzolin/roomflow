package br.com.pedrobuzolin.roomflow.api.validacoes;

import br.com.pedrobuzolin.roomflow.api.dto.SolicitacaoReservaDto;
import br.com.pedrobuzolin.roomflow.api.exception.ValidacaoException;
import br.com.pedrobuzolin.roomflow.api.model.StatusReserva;
import br.com.pedrobuzolin.roomflow.api.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoReservasConflitantes implements ValidacaoSolicitacaoReserva {
    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public void validar(SolicitacaoReservaDto dto) {
        boolean reservaConflitante = reservaRepository.buscarReservaConflitantePorSalaEHorario(StatusReserva.ATIVA, dto.idSala(),
                dto.dataInicio(), dto.dataFim());

        if (reservaConflitante) {
            throw new ValidacaoException("Sala já reservada para este horario");
        }
    }
}
