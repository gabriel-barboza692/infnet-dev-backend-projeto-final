package br.edu.infnet.backend.application.dto.emprestimo;

import java.time.LocalDate;

import br.edu.infnet.backend.domain.enums.EstadoDeConservacaoExemplar;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmprestimoResponse {

  private final Long exemplarId;
  private final String titulo;
  private final EstadoDeConservacaoExemplar estadoDeConservacaoExemplar;
  private final LocalDate dataDeDevolucao;
}
