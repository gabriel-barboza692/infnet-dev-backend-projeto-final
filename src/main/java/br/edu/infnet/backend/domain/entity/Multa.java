package br.edu.infnet.backend.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "multa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Multa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(name = "pago", nullable = false)
  private Boolean pago;

  @Column(name = "valor_pago", nullable = false)
  private BigDecimal valorPago;

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "emprestimo_id", nullable = false)
  private Emprestimo emprestimo;
}
