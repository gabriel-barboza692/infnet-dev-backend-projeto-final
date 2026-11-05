package br.edu.infnet.backend.domain.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_emprestimo")
@Data()
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Emprestimo {

  private final Integer PRAZO_EMPRESTIMO_DIAS = 14;

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotBlank
  @Column(name = "data_retirada", nullable = false)
  private LocalDate dataRetirada;

  @NotBlank
  @Column(name = "prazo_devolucao", nullable = false)
  private LocalDate prazoDevolucao;

  @Column(name = "data_efetiva_devolucao", nullable = true)
  private LocalDate dataEfetivaDevolucao;

  @ManyToOne
  @JoinColumn(name = "exemplar_id", nullable = false)
  private Exemplar exemplar;

  @ManyToOne
  @JoinColumn(name = "membro_id", nullable = false)
  private Membro membro;

  @OneToOne(mappedBy = "emprestimo")
  private List<Multa> multas = new ArrayList<>();

  public Emprestimo(Exemplar exemplar, Membro membro, LocalDate dataRetirada) {
    this.exemplar = exemplar;
    this.membro = membro;
    this.dataRetirada = dataRetirada;
    this.prazoDevolucao = dataRetirada.plusDays(PRAZO_EMPRESTIMO_DIAS);
  }

  public Emprestimo(Exemplar exemplar, Membro membro, LocalDate dataRetirada, LocalDate dataDevolucao) {
    this(exemplar, membro, dataRetirada);
    this.dataEfetivaDevolucao = dataDevolucao;
  }

  public long diasDeMulta() {
    if (this.dataEfetivaDevolucao == null)
      return 0L;
    long diasAtraso = Math.max(0, ChronoUnit.DAYS.between(prazoDevolucao, dataEfetivaDevolucao));
    return diasAtraso;
  }

}
