package br.edu.infnet.backend.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import br.edu.infnet.backend.config.AuthService;
import br.edu.infnet.backend.config.SecurityConfig;
import br.edu.infnet.backend.config.SecurityFilter;
import br.edu.infnet.backend.config.TokenService;
import br.edu.infnet.backend.domain.entity.Usuario;
import br.edu.infnet.backend.domain.repository.UserRepository;
import jakarta.servlet.Filter;

@SpringJUnitWebConfig(AuthFlowTest.TestConfig.class)
@TestPropertySource(properties = "biblioteca.flix.security.secret=auth-flow-test-secret")
class AuthFlowTest {
  @Configuration
  @EnableWebMvc
  @Import({SecurityConfig.class, SecurityFilter.class, TokenService.class, AuthService.class, AuthController.class})
  static class TestConfig {
    @Bean
    UserRepository userRepository() {
      return mock(UserRepository.class);
    }
  }

  @Autowired
  private WebApplicationContext context;
  @Autowired
  @Qualifier("springSecurityFilterChain")
  private Filter securityFilterChain;
  @Autowired
  private UserRepository repository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private TokenService tokenService;

  private MockMvc mvc;
  private final Map<String, Usuario> users = new HashMap<>();

  @BeforeEach
  void setUp() {
    users.clear();
    reset(repository);
    when(repository.save(any(Usuario.class))).thenAnswer(invocation -> {
      Usuario usuario = invocation.getArgument(0);
      usuario.setId(1L);
      users.put(usuario.getEmail(), usuario);
      return usuario;
    });
    when(repository.findByEmail(anyString())).thenAnswer(invocation ->
        Optional.ofNullable(users.get(invocation.getArgument(0))));
    mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(securityFilterChain).build();
  }

  @Test
  void cadastroSalvaHashELoginPermiteValidarToken() throws Exception {
    register();
    Usuario usuario = users.get("aluno@example.com");
    assertNotEquals("senha123", usuario.getPassword());
    assertTrue(passwordEncoder.matches("senha123", usuario.getPassword()));

    String response = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"email":"aluno@example.com","senha":"senha123"}
            """))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    String token = com.jayway.jsonpath.JsonPath.read(response, "$.token");
    var principal = tokenService.validateToken(token).orElseThrow();
    assertEquals(usuario.getId(), principal.userId());
    assertEquals(usuario.getEmail(), principal.email());
    mvc.perform(head("/auth/validate").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  void loginRecusaSenhaIncorreta() throws Exception {
    register();
    mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"email":"aluno@example.com","senha":"senha-errada"}
            """))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void loginRecusaUsuarioInexistente() throws Exception {
    mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"email":"inexistente@example.com","senha":"senha123"}
            """))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void validacaoRecusaTokenAusenteOuInvalido() throws Exception {
    mvc.perform(head("/auth/validate")).andExpect(status().isForbidden());
    mvc.perform(head("/auth/validate").header("Authorization", "Bearer invalido"))
        .andExpect(status().isForbidden());
  }

  private void register() throws Exception {
    mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"nome":"Aluno Teste","tipoUsuario":"ALUNO","matricula":"12345",
             "email":"aluno@example.com","password":"senha123"}
            """))
        .andExpect(status().isCreated());
  }
}
