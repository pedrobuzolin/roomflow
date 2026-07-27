package br.com.pedrobuzolin.roomflow.api.exception;

public class ValidacaoException extends ApiException {
    public ValidacaoException(String message) {
        super(message, TipoErro.VALIDACAO);
    }
}
