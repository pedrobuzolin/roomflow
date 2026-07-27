package br.com.pedrobuzolin.roomflow.api.exception;

public abstract class ApiException extends RuntimeException {
    private final TipoErro tipoErro;

    protected ApiException(String message, TipoErro tipoErro) {
        super(message);
        this.tipoErro = tipoErro;
    }

    public TipoErro getTipoErro() {
        return tipoErro;
    }
}
