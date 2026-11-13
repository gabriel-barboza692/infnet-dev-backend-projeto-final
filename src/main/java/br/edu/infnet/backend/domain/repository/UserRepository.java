package br.edu.infnet.backend.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.edu.infnet.backend.domain.entity.Usuario;

public interface UserRepository extends JpaRepository<Usuario, Long> {

  Optional<UserDetails> findByEmail(String email);
}
