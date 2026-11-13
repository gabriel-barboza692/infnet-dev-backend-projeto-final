package br.edu.infnet.backend.domain.entity;

import br.edu.infnet.backend.domain.enums.EstadoDeConservacaoExemplar;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "exemplar")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class Exemplar {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(name = "codigo_de_patrimonio", nullable = false)
  private String codigoDePatrimonio;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado_de_conservacao")
  private EstadoDeConservacaoExemplar estadoDeConservacaoExemplar;

  @ManyToOne
  @JoinColumn(name = "livro_id", nullable = false)
  private Livro livro;
}
