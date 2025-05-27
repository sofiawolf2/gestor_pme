package br.com.taurustech.gestor.exception;

import br.com.taurustech.gestor.security.ServletUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String erro = "erro";
        if (ex.getMessage().contains("java.util.Date")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ServletUtil.getJson(erro, "data inserida inválida"));
        if (ex.getMessage().contains("java.lang.Double")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ServletUtil.getJson(erro, "número decimal inserido inválido"));
        if (ex.getMessage().contains("java.lang.Boolean")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ServletUtil.getJson(erro, "valor do tipo verdadeiro ou falso inserido inválido"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ServletUtil.getJson(erro, "estrutura inválida"));
    }

    @ExceptionHandler(ObjetoNaoEncontradoException.class)
    public ResponseEntity<String> handleObjetoNaoEncontradoException (ObjetoNaoEncontradoException ex){
        return ResponseEntity.status(404).body(ServletUtil.getJson("erro", ex.getMessage()));
    }
}