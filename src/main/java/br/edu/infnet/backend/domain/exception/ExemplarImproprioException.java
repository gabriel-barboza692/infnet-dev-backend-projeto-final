package br.edu.infnet.backend.domain.exception;

public class ExemplarImproprioException extends RuntimeException {
  public ExemplarImproprioException() {
    super("Exemplar está impróprio para ser emprestado");
  }
}
