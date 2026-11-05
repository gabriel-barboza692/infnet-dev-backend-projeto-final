package br.edu.infnet.backend.application.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.infnet.backend.application.repository.EmprestimoRepository;
import br.edu.infnet.backend.application.repository.ExemplarRepository;
import br.edu.infnet.backend.application.repository.LivroRepository;
import br.edu.infnet.backend.application.repository.MembroRepository;
import br.edu.infnet.backend.application.repository.ReservaRepository;
import br.edu.infnet.backend.domain.entity.Emprestimo;
import br.edu.infnet.backend.domain.entity.Exemplar;
import br.edu.infnet.backend.domain.entity.Livro;
import br.edu.infnet.backend.domain.entity.Membro;
import br.edu.infnet.backend.domain.entity.Reserva;
import br.edu.infnet.backend.domain.exception.BadRequestException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservaService {

  private ReservaRepository reservaRepository;
  private MembroRepository membroRepository;
  private LivroRepository livroRepository;
  private ExemplarRepository exemplarRepository;
  private EmprestimoRepository emprestimoRepository;

  public Reserva fazerReserva(String livroId, String membroId, LocalDate dataReserva) {
    Membro membro = membroRepository.findById(membroId).orElseThrow(
        () -> new EntityNotFoundException(String.format("Membro com código: %s não foi encontrado", membroId)));
    List<Emprestimo> emprestimos = emprestimoRepository.findAll();
    List<Exemplar> exemplares = exemplarRepository.findAll();
    Livro livro = livroRepository.findById(livroId).orElseThrow(
        () -> new EntityNotFoundException((String.format("Livro com código: %s não foi encontrado", membroId))));
    if (!Exemplar.podeFazerReserva(emprestimos, exemplares, livro)) {
      throw new BadRequestException(
          String.format("O livro: %s já possui exemplares disponíveis para serem emprestados. ", livro.getNome()));
    }
    Reserva reserva = new Reserva(livro, membro, dataReserva);
    return reservaRepository.save(reserva);
  }
}
