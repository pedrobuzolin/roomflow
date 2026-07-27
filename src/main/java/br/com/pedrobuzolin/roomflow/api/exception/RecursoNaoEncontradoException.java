package br.com.pedrobuzolin.roomflow.api.exception;

public class RecursoNaoEncontradoException extends ApiException {
    public RecursoNaoEncontradoException(String message) {
        super(message, TipoErro.RECURSO_NAO_ENCONTRADO);
    }
}
