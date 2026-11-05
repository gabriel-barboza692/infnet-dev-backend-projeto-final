package br.edu.infnet.backend.domain.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_livro")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Livro {
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(length = 36)
  private String id;

  @NotBlank
  @Column(name = "autor", length = 50, nullable = false)
  private String autor;

  @NotBlank
  @Column(name = "isbn", length = 20, nullable = false)
  private String isbn;

  @NotBlank
  @Column(name = "nome", length = 50, nullable = false)
  private String nome;

  @OneToMany(mappedBy = "livro")
  private List<Exemplar> exemplares = new ArrayList<>();

  @OneToMany(mappedBy = "livro")
  private List<Reserva> reservas = new ArrayList<>();

}
