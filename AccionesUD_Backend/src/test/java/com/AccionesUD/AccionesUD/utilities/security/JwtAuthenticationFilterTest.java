package com.AccionesUD.AccionesUD.utilities.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    private JwtAuthenticationFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setup() {
        jwtService = mock(JwtService.class);
        userDetailsService = mock(UserDetailsService.class);
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);

        SecurityContextHolder.clearContext(); // limpia autenticación previa
    }

    @Test
    void doFilterInternal_NoToken_DoesNotAuthenticate() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

   @Test
void doFilterInternal_ValidToken_AuthenticatesUser() throws ServletException, IOException {
    String token = "Bearer fake.token.value";
    String username = "user@example.com";

    when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
    when(jwtService.getUsernameFromToken("fake.token.value")).thenReturn(username);

    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
    when(userDetails.getUsername()).thenReturn(username); // ← ESTA LÍNEA ES LA CLAVE

    when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
    when(jwtService.isTokenValid("fake.token.value", userDetails)).thenReturn(true);

    filter.doFilterInternal(request, response, chain);

    assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    assertEquals(username,
            ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

    verify(chain).doFilter(request, response);
}

    @Test
    void doFilterInternal_InvalidToken_DoesNotAuthenticate() throws ServletException, IOException {
        String token = "Bearer invalid.token";
        String username = "user@example.com";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        when(jwtService.getUsernameFromToken("invalid.token")).thenReturn(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid("invalid.token", userDetails)).thenReturn(false);

        filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_AlreadyAuthenticated_DoesNothing() throws ServletException, IOException {
        // Simula un contexto ya autenticado
        UsernamePasswordAuthenticationToken existingAuth =
                new UsernamePasswordAuthenticationToken("existingUser", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");
        when(jwtService.getUsernameFromToken("token")).thenReturn("user@example.com");

        filter.doFilterInternal(request, response, chain);

        // No se debe volver a autenticar
        verify(jwtService, never()).isTokenValid(any(), any());
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(chain).doFilter(request, response);
    }
}
