package br.edu.infnet.backend.domain.vo;

import br.edu.infnet.backend.domain.exception.DomainException;

public record ISBN(String value) {
  public ISBN {

    validarSeIsbnEhVaziaOuNula(value);
    validarSeIsbnSaoApenasNumeros(value);
    validarSeIsbnPossuiExtatos13Caracters(value);

  }

  private void validarSeIsbnEhVaziaOuNula(String value) {
    if (value == null) {
      throw new DomainException("ISBN não pode estar vazia");
    }
    String trimmedISBN = value.trim();
    if (trimmedISBN.length() == 0) {
      throw new DomainException("ISBN não pode estar vazia");
    }
  }

  private void validarSeIsbnSaoApenasNumeros(String value) {
    for (char c : value.toCharArray()) {
      if (!Character.isDigit(c)) {
        throw new DomainException("ISBN deve conter apenas números");
      }
    }
  }

  private void validarSeIsbnPossuiExtatos13Caracters(String value) {
    if (value.length() != 13) {
      throw new DomainException("ISBN deve conter 13 caracteres");
    }
  }
}
