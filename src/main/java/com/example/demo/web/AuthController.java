package com.example.demo.web;

import com.example.demo.security.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

  private final AuthenticationManager authManager;
  private final JwtUtil jwt;
  private final String cookieName;

  public AuthController(AuthenticationManager authManager, JwtUtil jwt,
                        @Value("${app.jwt.cookie-name}") String cookieName) {
    this.authManager = authManager; this.jwt = jwt; this.cookieName = cookieName;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginReq req){
    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(auth);

    String token = jwt.generate(req.getUsername());

    ResponseCookie rc = ResponseCookie.from(cookieName, token)
        .httpOnly(true).path("/").sameSite("Lax") // .secure(true) si usas HTTPS
        .build();

    return ResponseEntity.ok()
      .header(HttpHeaders.SET_COOKIE, rc.toString())
      .body(java.util.Map.of("token", token, "user", req.getUsername()));
  }

  @GetMapping("/profile")
  public java.util.Map<String,Object> profile(Authentication auth){
    return java.util.Map.of("user", auth.getName(), "roles", auth.getAuthorities());
  }

  @Data public static class LoginReq { private String username; private String password; }
}
