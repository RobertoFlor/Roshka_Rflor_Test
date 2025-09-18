package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService() {
    var u1 = User.withUsername("roberto").password("{noop}1234").roles("USER").build();
    var u2 = User.withUsername("admin").password("{noop}admin").roles("ADMIN").build();
    return new InMemoryUserDetailsManager(u1, u2);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwt) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/login").permitAll()
        .requestMatchers("/swagger-ui/**","/v3/api-docs/**","/actuator/**").permitAll()
        .requestMatchers("/api/profile").authenticated()
        .requestMatchers("/api/tasks/**").authenticated()
        .anyRequest().permitAll()
      )
      .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
