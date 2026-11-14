package br.edu.infnet.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.backend.domain.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

}
