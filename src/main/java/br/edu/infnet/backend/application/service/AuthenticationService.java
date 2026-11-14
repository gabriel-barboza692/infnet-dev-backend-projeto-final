package br.edu.infnet.backend.application.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.infnet.backend.config.TokenService;
import br.edu.infnet.backend.domain.entity.Usuario;
import br.edu.infnet.backend.domain.exception.UsuarioJaCadastradoException;
import br.edu.infnet.backend.domain.repository.UserRepository;
import br.edu.infnet.backend.presentation.dto.login.LoginRequest;
import br.edu.infnet.backend.presentation.dto.login.LoginResponse;
import br.edu.infnet.backend.presentation.dto.usuario.RegisterUsuarioRequest;
import br.edu.infnet.backend.presentation.dto.usuario.RegisterUsuarioResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final TokenService tokenService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public LoginResponse login(LoginRequest request) {
    UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.getEmail(),
        request.getSenha());
    Authentication authenticate = authenticationManager.authenticate(userAndPass);
    Usuario user = (Usuario) authenticate.getPrincipal();
    String token = tokenService.generateToken(user);
    return new LoginResponse(token);
  }

  public RegisterUsuarioResponse register(RegisterUsuarioRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new UsuarioJaCadastradoException();
    }
    Usuario usuario = Usuario
        .builder()
        .nome(request.getNome())
        .email(request.getEmail())
        .matricula(request.getMatricula())
        .password(passwordEncoder.encode(request.getPassword()))
        .tipoUsuario(request.getTipoUsuario())
        .build();
    try {
      userRepository.saveAndFlush(usuario);
    } catch (DataIntegrityViolationException exception) {
      // A restrição do banco também protege cadastros simultâneos do mesmo e-mail.
      if (userRepository.existsByEmail(request.getEmail())) {
        throw new UsuarioJaCadastradoException();
      }
      throw exception;
    }
    RegisterUsuarioResponse response = new RegisterUsuarioResponse();
    response.setNome(usuario.getNome());
    response.setEmail(usuario.getEmail());
    return response;
  }
}
