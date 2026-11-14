package br.edu.infnet.backend.application.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUsuarioResponse {
  @Email
  @NotNull
  private String email;

  @NotBlank
  @NotNull
  private String nome;
}
