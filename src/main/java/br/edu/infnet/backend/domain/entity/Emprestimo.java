package br.edu.infnet.backend.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Emprestimo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(name = "data_de_retirada", nullable = false)
  LocalDate dataDeRetirada;

  @Column(name = "prazo_de_devolucao", nullable = false)
  LocalDate prazoDeDevolucao;

  @Column(name = "data_efetiva_de_devolucao")
  LocalDate dataEfetivaDeDevolucao;

  @ManyToOne
  @JoinColumn(name = "exemplar_id", nullable = false)
  Exemplar exemplar;

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  Usuario usuario;
}
