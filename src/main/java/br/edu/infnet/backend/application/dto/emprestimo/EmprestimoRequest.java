package br.edu.infnet.backend.application.dto.emprestimo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmprestimoRequest {

  private final Long usuarioId;

  private final Long exemplarId;
}
