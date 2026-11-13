package br.edu.infnet.backend.config;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.edu.infnet.backend.domain.entity.Usuario;

@Component
public class TokenService {
  @Value("${biblioteca.flix.security.secret}")
  private String secret;

  public String generateToken(Usuario usuario) {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    return JWT.create()
        .withSubject(usuario.getEmail())
        .withClaim("userId", usuario.getId())
        .withExpiresAt(Instant.now()
            .plusSeconds(3600))
        .withIssuedAt(Instant.now())
        .sign(algorithm);
  }

  public Optional<JWTUserData> validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      DecodedJWT decode = JWT.require(algorithm).build().verify(token);

      return Optional.of(JWTUserData.builder()
          .email(decode.getSubject())
          .userId(decode.getClaim("userId").asLong())
          .build());
    } catch (JWTVerificationException e) {
      return Optional.empty();
    }
  }
}
