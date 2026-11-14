package br.edu.infnet.backend.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.edu.infnet.backend.application.dto.login.LoginRequest;
import br.edu.infnet.backend.application.dto.login.LoginResponse;
import br.edu.infnet.backend.application.dto.usuario.RegisterUsuarioRequest;
import br.edu.infnet.backend.application.dto.usuario.RegisterUsuarioResponse;
import br.edu.infnet.backend.application.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authenticationService.login(request));
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterUsuarioResponse> register(@Valid @RequestBody RegisterUsuarioRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(request));
  }

  @RequestMapping(value = "/validate", method = RequestMethod.HEAD)
  public ResponseEntity<Void> validate() {
    return ResponseEntity.ok().build();
  }

}
