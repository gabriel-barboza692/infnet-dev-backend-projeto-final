package br.edu.infnet.backend.domain.entity;

import br.edu.infnet.backend.domain.vo.AutorVO;
import br.edu.infnet.backend.domain.vo.ISBNVO;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "livro")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class Livro {
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String titulo;

  @Embedded
  @Column(nullable = false, length = 100)
  private AutorVO autor;

  @Embedded
  @Column(nullable = false, length = 100)
  private ISBNVO isbn;

  public Livro(String titulo, ISBNVO iSBN, AutorVO autor) {
    this.titulo = titulo;
    this.isbn = iSBN;
    this.autor = autor;
  }

}
