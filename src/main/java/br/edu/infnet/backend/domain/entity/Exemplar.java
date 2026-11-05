package br.edu.infnet.backend.domain.entity;

import java.util.ArrayList;
import java.util.List;

import br.edu.infnet.backend.domain.enums.EstadoDeConservacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_exemplar")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Exemplar {
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(length = 36)
  private String id;

  @NotBlank
  @Column(name = "codigo_de_patrimonio", nullable = false)
  private String codigoDePatrimonio;

  @NotBlank
  @Enumerated(EnumType.STRING)
  @Column(name = "estado_de_conservacao", nullable = false)
  private EstadoDeConservacao estadoDeConservacao;

  @ManyToOne
  @JoinColumn(name = "livro_id", nullable = false)
  Livro livro;

  @OneToMany(mappedBy = "exemplar")
  private List<Emprestimo> emprestimos = new ArrayList<>();

  public boolean isEstadoAdequadoDeUso() {
    if (this.estadoDeConservacao == EstadoDeConservacao.RUIM) {
      return false;
    }
    return true;
  }

  public static boolean podeFazerReserva(List<Emprestimo> emprestimos, List<Exemplar> exemplares, Livro livro) {
    // Retorna exemplares do livro escolhido que estejam em condições adequadas
    List<Exemplar> exemplaresDoLivroEscolhido = exemplares.stream()
        .filter(e -> e.getLivro().getId().equals(livro.getId()) && e.isEstadoAdequadoDeUso()).toList();
    List<Exemplar> exemplaresNaoEmprestadosDoLivroEscolhido = exemplaresDoLivroEscolhido.stream()
        .filter(exemplar -> emprestimos.stream()
            .noneMatch(emprestimo -> emprestimo.getExemplar().getId().equals(exemplar.getId())))
        .toList();
    if (exemplaresNaoEmprestadosDoLivroEscolhido.size() == 0) {
      return true;
    }
    return false;
  }

}
