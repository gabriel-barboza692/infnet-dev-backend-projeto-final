package br.edu.infnet.backend.presentation.exception;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.edu.infnet.backend.domain.exception.DomainException;
import br.edu.infnet.backend.domain.exception.ExemplarImproprioException;
import br.edu.infnet.backend.domain.exception.ExemplarJaEmprestadoException;
import br.edu.infnet.backend.domain.exception.RecursoNaoEncontradoException;
import br.edu.infnet.backend.domain.exception.UsuarioJaCadastradoException;
import br.edu.infnet.backend.domain.exception.UsuarioNaoPodeRealizarEmprestimo;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(UsuarioJaCadastradoException.class)
  public ProblemDetail handleUsuarioJaCadastrado(UsuarioJaCadastradoException exception, WebRequest request) {
    return problem(HttpStatus.CONFLICT, "Usuário já cadastrado", exception.getMessage(), request);
  }

  @ExceptionHandler(UsuarioNaoPodeRealizarEmprestimo.class)
  public ProblemDetail handlerUsuarioNaoPodeRealizarEmprestimo(UsuarioNaoPodeRealizarEmprestimo exception,
      WebRequest request) {
    return problem(HttpStatus.CONFLICT, "Usuário ultrapassou o limite de empréstimos", exception.getMessage(), request);
  }

  @ExceptionHandler(ExemplarImproprioException.class)
  public ProblemDetail handleExemplarImproprio(ExemplarImproprioException exception,
      WebRequest request) {
    return problem(HttpStatus.CONFLICT, "Exemplar impróprio", exception.getMessage(), request);
  }

  @ExceptionHandler(ExemplarJaEmprestadoException.class)
  public ProblemDetail handleExemplarJaEmprestado(ExemplarJaEmprestadoException exception,
      WebRequest request) {
    return problem(HttpStatus.CONFLICT, "Exemplar já emprestado", exception.getMessage(), request);
  }

  @ExceptionHandler(RecursoNaoEncontradoException.class)
  public ProblemDetail handleRecursoNaoEncontrado(RecursoNaoEncontradoException exception, WebRequest request) {
    return problem(HttpStatus.NOT_FOUND, "Recurso não encontrado", exception.getMessage(), request);
  }

  @ExceptionHandler(DomainException.class)
  public ProblemDetail handleDomain(DomainException exception, WebRequest request) {
    return problem(HttpStatus.UNPROCESSABLE_CONTENT, "Regra de negócio violada", exception.getMessage(), request);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ProblemDetail handleBadCredentials(BadCredentialsException exception, WebRequest request) {
    return problem(HttpStatus.UNAUTHORIZED, "Credenciais inválidas", "Usuário inexistente ou senha inválida", request);
  }

  @ExceptionHandler(InsufficientAuthenticationException.class)
  public ProblemDetail handleUnauthenticated(InsufficientAuthenticationException exception, WebRequest request) {
    return problem(HttpStatus.UNAUTHORIZED, "Autenticação necessária", "Informe um token de acesso válido.", request);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleAccessDenied(AccessDeniedException exception, WebRequest request) {
    return problem(HttpStatus.FORBIDDEN, "Acesso negado", "Você não tem permissão para acessar este recurso.", request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    ProblemDetail body = problem(HttpStatus.BAD_REQUEST, "Dados inválidos",
        "Verifique os campos informados.", request);
    body.setProperty("errors", exception.getBindingResult().getFieldErrors().stream()
        .map(error -> new FieldError(error.getField(), error.getDefaultMessage())).toList());
    return handleExceptionInternal(exception, body, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    ProblemDetail body = problem(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido",
        "Envie um JSON válido com os tipos de dados esperados.", request);
    return handleExceptionInternal(exception, body, headers, status, request);
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnexpected(Exception exception, WebRequest request) {
    log.error("Erro inesperado ao processar a requisição", exception);
    return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
        "Não foi possível processar a requisição. Tente novamente mais tarde.", request);
  }

  private ProblemDetail problem(HttpStatus status, String title, String detail, WebRequest request) {
    ProblemDetail body = ProblemDetail.forStatusAndDetail(status, detail);
    body.setTitle(title);
    if (request instanceof ServletWebRequest servletRequest) {
      body.setInstance(URI.create(servletRequest.getRequest().getRequestURI()));
    }
    return body;
  }

  public record FieldError(String field, String message) {
  }
}
