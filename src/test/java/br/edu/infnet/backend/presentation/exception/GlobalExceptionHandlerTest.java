package br.edu.infnet.backend.presentation.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.infnet.backend.domain.exception.DomainException;
import br.edu.infnet.backend.domain.exception.RecursoNaoEncontradoException;

class GlobalExceptionHandlerTest {
  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.standaloneSetup(new ErrorController())
        .setControllerAdvice(new GlobalExceptionHandler()).build();
  }

  @Test
  void regraDeDominioRetorna422() throws Exception {
    mvc.perform(get("/domain"))
        .andExpect(status().is(422))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("Regra de domínio violada"))
        .andExpect(jsonPath("$.instance").value("/domain"));
  }

  @Test
  void recursoNaoEncontradoRetorna404() throws Exception {
    mvc.perform(get("/missing"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.detail").value("Livro não encontrado"));
  }

  @Test
  void erroInesperadoNaoExpoeDetalhesInternos() throws Exception {
    mvc.perform(get("/unexpected"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.detail").value("Não foi possível processar a requisição. Tente novamente mais tarde."))
        .andExpect(jsonPath("$.stackTrace").doesNotExist());
  }

  @Test
  void preservaStatusDosErrosHttpDoSpring() throws Exception {
    mvc.perform(post("/domain"))
        .andExpect(status().isMethodNotAllowed())
        .andExpect(jsonPath("$.status").value(405));
  }

  @RestController
  static class ErrorController {
    @GetMapping("/domain")
    public void domain() {
      throw new DomainException("Regra de domínio violada");
    }

    @GetMapping("/missing")
    public void missing() {
      throw new RecursoNaoEncontradoException("Livro não encontrado");
    }

    @GetMapping("/unexpected")
    public void unexpected() {
      throw new IllegalStateException("Detalhes internos que não devem sair na resposta");
    }
  }
}
