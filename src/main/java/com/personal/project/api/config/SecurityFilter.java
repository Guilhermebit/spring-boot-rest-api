package com.personal.project.api.config;

import com.personal.project.api.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    JWTUtil jwtUtil;

    @Lazy
    @Autowired
    AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
          var token = jwtUtil.recoverToken(request);
          if(token != null){
              var login = jwtUtil.validateToken(token);
              authenticationService.authenticateUserByTokenData(login);
          }
          filterChain.doFilter(request, response); // goes to the next filter in the filter chain
    }
}
