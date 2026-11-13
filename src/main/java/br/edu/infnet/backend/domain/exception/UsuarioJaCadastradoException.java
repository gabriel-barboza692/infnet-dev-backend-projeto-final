package br.edu.infnet.backend.domain.exception;

public class UsuarioJaCadastradoException extends DomainException {
  public UsuarioJaCadastradoException() {
    super("Já existe um usuário cadastrado com este e-mail.");
  }
}
