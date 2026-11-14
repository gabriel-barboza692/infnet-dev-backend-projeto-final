package br.edu.infnet.backend.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.infnet.backend.domain.entity.Emprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
  @Query("""
        SELECT e
          FROM Emprestimo e
         WHERE e.usuario.id = :usuarioId
           AND e.dataEfetivaDeDevolucao IS NULL
      """)
  List<Emprestimo> buscarEmprestimosPendentes(@Param("usuarioId") Long usuarioId);

  @Query("""
        SELECT CASE WHEN count(e) > 0 THEN true ELSE false END
          FROM Emprestimo e
         WHERE e.exemplar.id = :exemplarId
           AND e.dataEfetivaDeDevolucao IS NULL

      """)
  boolean exemplarEstaEmprestado(@Param("exemplarId") Long exemplarId);
}
