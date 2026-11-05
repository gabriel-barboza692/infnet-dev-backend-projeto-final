package br.edu.infnet.backend.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.backend.domain.entity.Emprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, String> {

}
