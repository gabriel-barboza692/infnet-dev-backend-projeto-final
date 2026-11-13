package br.edu.infnet.backend.domain.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import br.edu.infnet.backend.domain.exception.DomainException;

public class ISBNTest {
  @Test
  void deveRetornarUmErroQuandoISBNEstaVazioOuNuloOuComEspacosEmBranco() {
    String isbnEspacoEmBranco = " ";
    String isbnNula = null;
    String isbnVazia = "";

    DomainException exceptionEspacoEmBranco = assertThrows(DomainException.class, () -> new ISBNVO(isbnEspacoEmBranco));
    DomainException exceptionIsbnNula = assertThrows(DomainException.class, () -> new ISBNVO(isbnNula));
    DomainException exceptionIsbnVazia = assertThrows(DomainException.class, () -> new ISBNVO(isbnVazia));
    assertEquals("ISBN não pode estar vazia", exceptionEspacoEmBranco.getMessage());
    assertEquals("ISBN não pode estar vazia", exceptionIsbnNula.getMessage());
    assertEquals("ISBN não pode estar vazia", exceptionIsbnVazia.getMessage());
  }

  @Test
  void deveRetornarUmErroQuandoISBNPossuirCaracteresNaoNumericos() {
    String isbnComLetras = "123sdsfdsa#";
    DomainException exception = assertThrows(DomainException.class, () -> new ISBNVO(isbnComLetras));
    assertEquals("ISBN deve conter apenas números", exception.getMessage());
  }

  @Test
  void deveRetornarUmErroSeNaoPossuirExatos13Digitos() {
    String isbnInvalido = "123456789111";
    DomainException exception = assertThrows(DomainException.class, () -> new ISBNVO(isbnInvalido));
    assertEquals("ISBN deve conter 13 caracteres", exception.getMessage());
  }

}
