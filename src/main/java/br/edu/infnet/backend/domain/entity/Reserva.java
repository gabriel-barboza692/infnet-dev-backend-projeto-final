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
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_reserva")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reserva {
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotBlank
  @Column(name = "data_reserva", nullable = false)
  private LocalDate dataReserva;

  @ManyToOne
  @JoinColumn(name = "livro_id", nullable = false)
  private Livro livro;

  @ManyToOne
  @JoinColumn(name = "membro_id", nullable = false)
  private Membro membro;

  public Reserva(Livro livro, Membro membro, LocalDate dataReserva) {
    this.livro = livro;
    this.membro = membro;
    this.dataReserva = dataReserva;
  }
}
