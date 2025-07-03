package com.AccionesUD.AccionesUD.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.AccionesUD.AccionesUD.utilities.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(Customizer.withDefaults())  // Usar la nueva sintaxis recomendada para CORS
            .csrf(csrf -> 
                csrf.disable())  // Desactivar CSRF si no lo necesitas
            .authorizeHttpRequests(authRequest ->
                authRequest
                    .requestMatchers("/auth/**").permitAll()  // Rutas públicas para autenticación
                    .requestMatchers("/api/**").permitAll()   // Rutas públicas para la API
                    .requestMatchers("/acciones/scrap").permitAll()  // Rutas públicas para scrapping
                    .anyRequest().authenticated()  // Requiere autenticación para el resto de las rutas
            )
            .sessionManagement(sessionManager -> 
                sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Usar sesión sin estado
            )
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // Añadir filtro JWT
            .build();
    }
}
