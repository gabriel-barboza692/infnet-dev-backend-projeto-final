package br.edu.infnet.backend.domain.entity;

import java.util.ArrayList;
import java.util.List;

import br.edu.infnet.backend.domain.enums.TipoAluno;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_membro")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Membro {
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotBlank
  @Column(name = "nome", length = 100)
  private String nome;

  @NotBlank
  @Column(name = "matricula", length = 100)
  private String matricula;

  @NotBlank
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_aluno", nullable = false)
  private TipoAluno tipoAluno;

  @OneToMany(mappedBy = "membro")
  private List<Emprestimo> emprestimos = new ArrayList<>();

  @OneToMany(mappedBy = "membro")
  private List<Reserva> reservas = new ArrayList<>();

  @OneToMany(mappedBy = "membro")
  private List<Multa> multas = new ArrayList<>();
}
