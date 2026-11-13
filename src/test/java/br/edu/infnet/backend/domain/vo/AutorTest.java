package br.edu.infnet.backend.domain.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import br.edu.infnet.backend.domain.exception.DomainException;

public class AutorTest {
  @Test
  void naoPodePermitirAutorComNomeVazioOuNulo() {
    String autorNulo = null;
    String autorVazio = "";
    String autorComEspacoEmBranco = "  ";

    DomainException exceptionAutorNulo = assertThrows(DomainException.class, () -> new AutorVO(autorNulo));
    DomainException exceptionAutorVazio = assertThrows(DomainException.class, () -> new AutorVO(autorVazio));
    DomainException exceptionAutorComEspacoEmBranco = assertThrows(DomainException.class,
        () -> new AutorVO(autorComEspacoEmBranco));
    assertEquals("Autor não pode possuir nome nulo", exceptionAutorNulo.getMessage());
    assertEquals("Autor não pode possuir nome vazio", exceptionAutorVazio.getMessage());
    assertEquals("Autor não pode possuir nome vazio", exceptionAutorComEspacoEmBranco.getMessage());
  }
}
