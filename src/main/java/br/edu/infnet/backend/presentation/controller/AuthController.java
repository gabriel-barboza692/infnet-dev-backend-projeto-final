package br.edu.infnet.backend.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.edu.infnet.backend.config.TokenService;
import br.edu.infnet.backend.domain.entity.Usuario;
import br.edu.infnet.backend.domain.repository.UserRepository;
import br.edu.infnet.backend.presentation.dto.login.LoginRequest;
import br.edu.infnet.backend.presentation.dto.login.LoginResponse;
import br.edu.infnet.backend.presentation.dto.usuario.RegisterUsuarioRequest;
import br.edu.infnet.backend.presentation.dto.usuario.RegisterUsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final TokenService tokenService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    try {
      UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.getEmail(),
          request.getSenha());
      Authentication authenticate = authenticationManager.authenticate(userAndPass);

      Usuario user = (Usuario) authenticate.getPrincipal();

      String token = tokenService.generateToken(user);
      LoginResponse loginResponse = new LoginResponse();
      loginResponse.setToken(token);
      return ResponseEntity.ok(loginResponse);
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário inexistente ou senha inválida");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterUsuarioResponse> register(@Valid @RequestBody RegisterUsuarioRequest request) {
    Usuario usuario = Usuario
        .builder()
        .nome(request.getNome())
        .email(request.getEmail())
        .matricula(request.getMatricula())
        .password(passwordEncoder.encode(request.getPassword()))
        .tipoUsuario(request.getTipoUsuario())
        .build();
    userRepository.save(usuario);
    RegisterUsuarioResponse registerUsuarioResponse = new RegisterUsuarioResponse();
    registerUsuarioResponse.setNome(usuario.getNome());
    registerUsuarioResponse.setEmail(usuario.getEmail());
    return ResponseEntity.status(HttpStatus.CREATED).body(registerUsuarioResponse);
  }

  @RequestMapping(value = "/validate", method = RequestMethod.HEAD)
  public ResponseEntity<Void> validate() {
    return ResponseEntity.ok().build();
  }

}
