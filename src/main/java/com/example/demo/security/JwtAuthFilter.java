package com.example.demo.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtUtil jwt;
  private final UserDetailsService uds;

  public JwtAuthFilter(JwtUtil jwt, UserDetailsService uds) { this.jwt = jwt; this.uds = uds; }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {

    String token = null;
    String bearer = req.getHeader(HttpHeaders.AUTHORIZATION);
    if (bearer != null && bearer.startsWith("Bearer ")) token = bearer.substring(7);
    if (token == null && req.getCookies()!=null) {
      for (Cookie c: req.getCookies()) if ("AUTH_TOKEN".equals(c.getName())) { token = c.getValue(); break; }
    }

    if (token != null && SecurityContextHolder.getContext().getAuthentication()==null) {
      try {
        String username = jwt.validateAndGetSubject(token);
        UserDetails ud = uds.loadUserByUsername(username);
        var auth = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
      } catch (Exception ignored) { }
    }
    chain.doFilter(req, res);
  }
}
