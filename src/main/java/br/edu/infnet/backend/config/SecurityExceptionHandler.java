package br.edu.infnet.backend.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
  private final HandlerExceptionResolver resolver;

  public SecurityExceptionHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
    // Leva as falhas dos filtros ao mesmo contrato de erros usado pelos controllers.
    resolver.resolveException(request, response, null,
        new InsufficientAuthenticationException("Autenticação necessária", exception));
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
    resolver.resolveException(request, response, null, exception);
  }
}
