
package br.com.pedrobuzolin.roomflow.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ProblemDetail tratarApiException(ApiException ex) {
        HttpStatus status = statusParaErro(ex.getTipoErro());

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(status, ex.getMessage());

        problem.setTitle("Erro na aplicação");
        problem.setProperty("tipoErro", ex.getTipoErro());
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarErrosDeValidacao(
            MethodArgumentNotValidException ex) {

        ProblemDetail problem = criarProblem(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Um ou mais campos possuem valores inválidos."
        );

        List<Map<String, String>> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> Map.of(
                        "campo", erro.getField(),
                        "mensagem", erro.getDefaultMessage()
                ))
                .toList();

        problem.setProperty("erros", erros);

        return problem;
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class
    })
    public ProblemDetail tratarRequisicaoInvalida(Exception ex) {
        return criarProblem(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida",
                "A requisição contém parâmetros ausentes ou valores inválidos."
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarViolacaoDeIntegridade(
            DataIntegrityViolationException ex) {

        log.warn("Violação de integridade dos dados", ex);

        return criarProblem(
                HttpStatus.CONFLICT,
                "Conflito de dados",
                "A operação não pode ser realizada devido a um conflito com os dados existentes."
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail tratarErroInterno(Exception ex) {
        log.error("Erro inesperado na aplicação", ex);

        return criarProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "Ocorreu um erro interno ao processar a requisição."
        );
    }

    private ProblemDetail criarProblem(
            HttpStatus status,
            String titulo,
            String detalhe) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(status, detalhe);

        problem.setTitle(titulo);
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    private HttpStatus statusParaErro(TipoErro tipoErro) {
        return switch (tipoErro) {
            case VALIDACAO -> HttpStatus.BAD_REQUEST;
            case RECURSO_NAO_ENCONTRADO -> HttpStatus.NOT_FOUND;
        };
    }
}
