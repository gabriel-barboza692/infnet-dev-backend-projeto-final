package br.edu.infnet.backend.application.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.infnet.backend.application.repository.EmprestimoRepository;
import br.edu.infnet.backend.application.repository.ExemplarRepository;
import br.edu.infnet.backend.application.repository.MembroRepository;
import br.edu.infnet.backend.application.repository.MultaRepository;
import br.edu.infnet.backend.domain.entity.Emprestimo;
import br.edu.infnet.backend.domain.entity.Exemplar;
import br.edu.infnet.backend.domain.entity.Membro;
import br.edu.infnet.backend.domain.entity.Multa;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmprestimoService {
  private Integer LIMITE_DE_EMPRESTIMOS = 3;

  private EmprestimoRepository emprestimoRepository;

  private ExemplarRepository exemplarRepository;

  private MembroRepository membroRepository;

  private MultaRepository multaRepository;

  public Emprestimo tomarEmprestimo(String exemplarId, String membroId, LocalDate dataRetirada) {
    List<Emprestimo> emprestimos = emprestimoRepository.findAll();
    Exemplar exemplar = exemplarRepository.findById(exemplarId).orElseThrow(
        () -> new EntityNotFoundException(String.format("Exemplar com código %s não foi encontrada", exemplarId)));

    if (!exemplar.isEstadoAdequadoDeUso()) {
      throw new IllegalStateException("O Exemplar não está em estado adequado de uso");
    }

    boolean isExemplarEmprestado = emprestimos.stream().anyMatch(e -> e.getExemplar().getId().equals(exemplar.getId()));
    if (isExemplarEmprestado) {
      throw new IllegalStateException("O exemplar já está emprestado");
    }

    Membro membro = membroRepository.findById(membroId).orElseThrow(
        () -> new EntityNotFoundException(String.format("Membro com código %s não foi encontrado", membroId)));

    Long emprestimosAtivosLen = emprestimos.stream().filter(e -> e.getMembro().getId().equals(membro.getId())).count();

    if (emprestimosAtivosLen > LIMITE_DE_EMPRESTIMOS) {
      throw new IllegalStateException(String.format("O membro: %s possui %s empréstimos ativos. O limite são %s",
          membro.getNome(), emprestimosAtivosLen, LIMITE_DE_EMPRESTIMOS));
    }

    Emprestimo emprestimo = new Emprestimo(exemplar, membro, dataRetirada);
    Emprestimo emprestimoSalvo = emprestimoRepository.save(emprestimo);
    return emprestimoSalvo;
  }

  public Emprestimo devolverEmprestimo(String emprestimoId, LocalDate dataDevolucao) {
    Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId).orElseThrow(
        () -> new EntityNotFoundException(String.format("Empréstimo com código %s não foi encontrado", emprestimoId)));

    emprestimo.setDataEfetivaDevolucao(dataDevolucao);
    Membro membro = membroRepository.findById(emprestimo.getMembro().getId())
        .orElseThrow(() -> new EntityNotFoundException(
            String.format("Membro com código %s não foi encontrado", emprestimo.getMembro().getId())));

    Multa multa = new Multa(emprestimo, membro);
    if (multa.getValor().compareTo(BigDecimal.ZERO) > 0) {
      multaRepository.save(multa);
    }
    return emprestimoRepository.save(emprestimo);
  }
}
