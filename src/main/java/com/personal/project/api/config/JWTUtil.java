package com.personal.project.api.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.personal.project.api.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JWTUtil {

    @Value("${api.security.token.secret}")
    private String secret;

    public String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ","");
    }

    public String generateToken(User user) {
          try{
              Algorithm algorithm = Algorithm.HMAC256(secret);
              return JWT.create()
                      .withIssuer("spring-rest-api")
                      .withSubject(user.getLogin())
                      .withExpiresAt(genExpirationData())
                      .sign(algorithm);
          } catch(JWTCreationException exception) {
              throw new RuntimeException("Error while generating token.", exception);
          }
    }

    public String validateToken(String token) {
          try{
             Algorithm algorithm = Algorithm.HMAC256(secret);
             return JWT.require(algorithm)
                     .withIssuer("spring-rest-api")
                     .build()
                     .verify(token)
                     .getSubject();
          } catch(JWTCreationException exception) {
              return "";
          }
    }

    // sets a time for the token to expire
    private Instant genExpirationData() {
          return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
