package br.edu.infnet.backend.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_multa")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Multa {

  private static final BigDecimal MULTA_POR_DIA = new BigDecimal("5.00");

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotBlank
  @Column(name = "valor", nullable = false, precision = 10, scale = 2)
  private BigDecimal valor;

  @NotNull
  @Column(name = "is_pago", nullable = false)
  private boolean isPago = false;

  @ManyToOne
  @JoinColumn(name = "membro_id")
  private Membro membro;

  @OneToOne
  @JoinColumn(name = "emprestimo_id")
  private Emprestimo emprestimo;

  public Multa(Emprestimo emprestimo, Membro membro) {
    this.emprestimo = emprestimo;
    this.membro = membro;
    calcularValorMulta();
  }

  private void calcularValorMulta() {
    long diasAtraso = emprestimo.diasDeMulta();
    this.valor = MULTA_POR_DIA.multiply(BigDecimal.valueOf(diasAtraso));
  }
}
