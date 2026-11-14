package br.edu.infnet.backend.application.dto.usuario;

import br.edu.infnet.backend.domain.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterUsuarioRequest {
  @NotBlank
  @NotNull
  @Size(min = 5)
  private String nome;

  @NotNull
  private TipoUsuario tipoUsuario;

  @NotNull
  private String matricula;

  @Email
  @NotNull
  private String email;

  @NotBlank
  @NotNull
  @Size(min = 5)
  private String password;
}
