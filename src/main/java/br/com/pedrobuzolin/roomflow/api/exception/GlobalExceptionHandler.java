package br.com.pedrobuzolin.roomflow.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ProblemDetail tratarApiException(ApiException ex) {
        HttpStatus status = statusParaErro(ex.getTipoErro());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problem.setProperty("tipoErro", ex.getTipoErro());
        problem.setProperty("timestamp", LocalDateTime.now());
        return problem;
    }

    private HttpStatus statusParaErro(TipoErro tipoErro) {
        return switch (tipoErro) {
            case VALIDACAO -> HttpStatus.BAD_REQUEST;
            case RECURSO_NAO_ENCONTRADO -> HttpStatus.NOT_FOUND;
        };
    }
}
