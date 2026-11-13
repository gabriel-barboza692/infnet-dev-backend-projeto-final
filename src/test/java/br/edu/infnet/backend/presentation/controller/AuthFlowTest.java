package br.edu.infnet.backend.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import br.edu.infnet.backend.config.AuthService;
import br.edu.infnet.backend.config.SecurityConfig;
import br.edu.infnet.backend.config.SecurityExceptionHandler;
import br.edu.infnet.backend.config.SecurityFilter;
import br.edu.infnet.backend.config.TokenService;
import br.edu.infnet.backend.domain.entity.Usuario;
import br.edu.infnet.backend.domain.repository.UserRepository;
import br.edu.infnet.backend.presentation.exception.GlobalExceptionHandler;
import jakarta.servlet.Filter;

@SpringJUnitWebConfig(AuthFlowTest.TestConfig.class)
@TestPropertySource(properties = "biblioteca.flix.security.secret=auth-flow-test-secret")
class AuthFlowTest {
  @Configuration
  @EnableWebMvc
  @Import({SecurityConfig.class, SecurityFilter.class, TokenService.class, AuthService.class, AuthController.class,
      GlobalExceptionHandler.class, SecurityExceptionHandler.class})
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
  @Autowired
  private SecurityExceptionHandler securityExceptionHandler;

  private MockMvc mvc;
  private final Map<String, Usuario> users = new HashMap<>();

  @BeforeEach
  void setUp() {
    users.clear();
    reset(repository);
    when(repository.saveAndFlush(any(Usuario.class))).thenAnswer(invocation -> {
      Usuario usuario = invocation.getArgument(0);
      usuario.setId(1L);
      users.put(usuario.getEmail(), usuario);
      return usuario;
    });
    when(repository.findByEmail(anyString())).thenAnswer(invocation ->
        Optional.ofNullable(users.get(invocation.getArgument(0))));
    when(repository.existsByEmail(anyString())).thenAnswer(invocation ->
        users.containsKey(invocation.getArgument(0)));
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
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.status").value(401))
        .andExpect(jsonPath("$.detail").value("Usuário inexistente ou senha inválida"))
        .andExpect(jsonPath("$.instance").value("/auth/login"));
  }

  @Test
  void loginRecusaUsuarioInexistente() throws Exception {
    mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"email":"inexistente@example.com","senha":"senha123"}
            """))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.detail").value("Usuário inexistente ou senha inválida"));
  }

  @Test
  void validacaoRecusaTokenAusenteOuInvalido() throws Exception {
    mvc.perform(head("/auth/validate")).andExpect(status().isUnauthorized());
    mvc.perform(head("/auth/validate").header("Authorization", "Bearer invalido"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void errosDosFiltrosUsamProblemDetail() throws Exception {
    mvc.perform(post("/recurso-protegido"))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.status").value(401))
        .andExpect(jsonPath("$.instance").value("/recurso-protegido"));
    mvc.perform(post("/recurso-protegido").header("Authorization", "Bearer invalido"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(401));
  }

  @Test
  void acessoNegadoUsaMesmoContrato() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/recurso-restrito");
    MockHttpServletResponse response = new MockHttpServletResponse();
    securityExceptionHandler.handle(request, response, new AccessDeniedException("detalhe interno"));
    assertEquals(403, response.getStatus());
    assertTrue(response.getContentType().startsWith(MediaType.APPLICATION_PROBLEM_JSON_VALUE));
    int status = com.jayway.jsonpath.JsonPath.read(response.getContentAsString(), "$.status");
    assertEquals(403, status);
    assertEquals("/recurso-restrito", com.jayway.jsonpath.JsonPath.read(response.getContentAsString(), "$.instance"));
  }

  @Test
  void cadastroDuplicadoRetornaConflito() throws Exception {
    when(repository.existsByEmail("aluno@example.com")).thenReturn(true);
    mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(registerJson()))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409));
    verify(repository, never()).saveAndFlush(any(Usuario.class));
  }

  @Test
  void cadastroSimultaneoTambemRetornaConflito() throws Exception {
    when(repository.existsByEmail("aluno@example.com")).thenReturn(false, true);
    when(repository.saveAndFlush(any(Usuario.class)))
        .thenThrow(new DataIntegrityViolationException("unique constraint"));
    mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(registerJson()))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409));
  }

  @Test
  void validacaoInformaCamposSemExporSenha() throws Exception {
    mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"email":"email-invalido","senha":""}
            """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.errors[?(@.field == 'email')]").isNotEmpty())
        .andExpect(jsonPath("$.errors[?(@.field == 'senha')]").isNotEmpty())
        .andExpect(jsonPath("$.errors[0].rejectedValue").doesNotExist());
    verify(repository, never()).findByEmail(anyString());
  }

  @Test
  void jsonMalformadoRetornaBadRequest() throws Exception {
    mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("{"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.detail").value("Envie um JSON válido com os tipos de dados esperados."));
  }

  private void register() throws Exception {
    mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
        .content(registerJson()))
        .andExpect(status().isCreated());
  }

  private String registerJson() {
    return """
        {"nome":"Aluno Teste","tipoUsuario":"ALUNO","matricula":"12345",
         "email":"aluno@example.com","password":"senha123"}
        """;
  }
}
