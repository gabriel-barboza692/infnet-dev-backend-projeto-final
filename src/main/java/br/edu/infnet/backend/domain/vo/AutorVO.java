package br.edu.infnet.backend.domain.vo;

import br.edu.infnet.backend.domain.exception.DomainException;

public record AutorVO(String nome) {
  public AutorVO {
    if (nome == null) {
      throw new DomainException("Autor não pode possuir nome nulo");
    }
    if (nome.trim().length() == 0) {
      throw new DomainException("Autor não pode possuir nome vazio");
    }
  }
}
