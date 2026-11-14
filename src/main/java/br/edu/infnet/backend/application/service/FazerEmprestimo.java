package br.edu.infnet.backend.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.infnet.backend.application.dto.emprestimo.EmprestimoRequest;
import br.edu.infnet.backend.application.dto.emprestimo.EmprestimoResponse;
import br.edu.infnet.backend.domain.entity.Emprestimo;
import br.edu.infnet.backend.domain.entity.Exemplar;
import br.edu.infnet.backend.domain.entity.Usuario;
import br.edu.infnet.backend.domain.enums.EstadoDeConservacaoExemplar;
import br.edu.infnet.backend.domain.exception.ExemplarImproprioException;
import br.edu.infnet.backend.domain.exception.ExemplarJaEmprestadoException;
import br.edu.infnet.backend.domain.exception.RecursoNaoEncontradoException;
import br.edu.infnet.backend.domain.exception.UsuarioNaoPodeRealizarEmprestimo;
import br.edu.infnet.backend.domain.repository.EmprestimoRepository;
import br.edu.infnet.backend.domain.repository.ExemplarRepository;
import br.edu.infnet.backend.domain.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FazerEmprestimo {
  private final int LIMITE_EMPRESTIMOS_ATIVOS = 3;
  private final int PRAZO_DEVOLUCAO = 14;

  private final EmprestimoRepository emprestimoRepository;
  private final UserRepository userRepository;
  private final ExemplarRepository exemplarRepository;

  public EmprestimoResponse fazerEmprestimo(EmprestimoRequest emprestimoRequest) {
    Exemplar exemplar = buscarExemplarPorId(emprestimoRequest.getExemplarId());
    validarEstadoDoExemplar(exemplar);
    validarSeOExemplarJaEstaEmprestado(exemplar);
    Usuario usuario = buscarUsuarioPorId(emprestimoRequest.getUsuarioId());
    List<Emprestimo> emprestimosPendentes = emprestimoRepository.buscarEmprestimosPendentes(usuario.getId());
    validarSeOLimiteDeEmprestimoFoiAtingido(emprestimosPendentes);
    return fazerEmprestimoQuandoValido(exemplar, usuario);
  }

  private Exemplar buscarExemplarPorId(Long exemplarId) {
    return exemplarRepository.findById(exemplarId)
        .orElseThrow(() -> new RecursoNaoEncontradoException(
            "Exemplar com id: " + exemplarId + " não encontrado"));
  }

  private void validarEstadoDoExemplar(Exemplar exemplar) {
    if (exemplar.getEstadoDeConservacaoExemplar() == EstadoDeConservacaoExemplar.RUIM) {
      throw new ExemplarImproprioException();
    }
  }

  private void validarSeOExemplarJaEstaEmprestado(Exemplar exemplar) {
    if (emprestimoRepository.exemplarEstaEmprestado(exemplar.getId())) {
      throw new ExemplarJaEmprestadoException("O exemplar com id: " + exemplar.getId() + " já está emprestado");
    }
  }

  private Usuario buscarUsuarioPorId(Long usuarioId) {
    return userRepository.findById(usuarioId)
        .orElseThrow(() -> new RecursoNaoEncontradoException(
            "Usuário com id: " + usuarioId + " não encontrado"));
  }

  private void validarSeOLimiteDeEmprestimoFoiAtingido(List<Emprestimo> emprestimosPendentes) {
    if (emprestimosPendentes.size() >= LIMITE_EMPRESTIMOS_ATIVOS) {
      throw new UsuarioNaoPodeRealizarEmprestimo(
          "O limite de empréstimos ativos: " + LIMITE_EMPRESTIMOS_ATIVOS + "foi atingido");
    }

  }

  private EmprestimoResponse fazerEmprestimoQuandoValido(Exemplar exemplar, Usuario usuario) {
    LocalDate agora = LocalDate.now();
    LocalDate prazoDevolucao = LocalDate.now().plusDays(PRAZO_DEVOLUCAO);
    Emprestimo emprestimo = Emprestimo
        .builder()
        .dataDeRetirada(agora)
        .prazoDeDevolucao(prazoDevolucao)
        .dataEfetivaDeDevolucao(null)
        .exemplar(exemplar)
        .usuario(usuario)
        .build();
    emprestimoRepository.save(emprestimo);

    EmprestimoResponse emprestimoResponse = new EmprestimoResponse(exemplar.getId(), exemplar.getLivro().getTitulo(),
        exemplar.getEstadoDeConservacaoExemplar(), prazoDevolucao);
    return emprestimoResponse;
  }

}
