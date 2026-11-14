package br.edu.infnet.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.backend.domain.entity.Emprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

}
