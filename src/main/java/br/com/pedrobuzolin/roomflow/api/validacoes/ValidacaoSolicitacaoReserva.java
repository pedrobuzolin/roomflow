package br.com.pedrobuzolin.roomflow.api.validacoes;

import br.com.pedrobuzolin.roomflow.api.dto.SolicitacaoReservaDto;

public interface ValidacaoSolicitacaoReserva {
    void validar(SolicitacaoReservaDto dto);
}
