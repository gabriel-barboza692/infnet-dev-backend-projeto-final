package br.edu.infnet.backend.domain.exception;

public class UsuarioNaoPodeRealizarEmprestimo extends RuntimeException {
  public UsuarioNaoPodeRealizarEmprestimo(String message) {
    super(message);
  }
}
