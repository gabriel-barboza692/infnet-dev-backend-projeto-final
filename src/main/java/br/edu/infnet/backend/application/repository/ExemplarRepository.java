package br.edu.infnet.backend.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.backend.domain.entity.Exemplar;

public interface ExemplarRepository extends JpaRepository<Exemplar, String> {

}
