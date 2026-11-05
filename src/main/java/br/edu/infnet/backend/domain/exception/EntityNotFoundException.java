package br.edu.infnet.backend.domain.exception;

public class EntityNotFoundException extends RuntimeException {
  EntityNotFoundException(String message) {
    super(message);
  }
}
