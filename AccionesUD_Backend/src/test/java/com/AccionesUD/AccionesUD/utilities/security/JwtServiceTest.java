package com.AccionesUD.AccionesUD.utilities.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import com.AccionesUD.AccionesUD.dto.orders.OrderRequestDTO;
import com.AccionesUD.AccionesUD.utilities.orders.OrderType;
import com.AccionesUD.AccionesUD.utilities.orders.OrderValidator;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        userDetails = new UserDetails() {
            @Override public String getUsername() { return "user@example.com"; }
            @Override public boolean isEnabled() { return true; }
            @Override public boolean isCredentialsNonExpired() { return true; }
            @Override public boolean isAccountNonLocked() { return true; }
            @Override public boolean isAccountNonExpired() { return true; }
            @Override public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() {
                return java.util.Collections.emptyList();
            }
            @Override public String getPassword() { return "password"; }
        };
    }

    @Test
    void generateToken_And_ExtractUsername_ShouldMatch() {
        String token = jwtService.getToken(userDetails);
        String extractedUsername = jwtService.getUsernameFromToken(token);

        assertEquals("user@example.com", extractedUsername);
    }

    @Test
    void isTokenValid_ShouldReturnTrueForValidToken() {
        String token = jwtService.getToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_ShouldReturnFalseForWrongUsername() {
        String token = jwtService.getToken(userDetails);

        UserDetails otherUser = new UserDetails() {
            @Override public String getUsername() { return "otro@correo.com"; }
            @Override public boolean isEnabled() { return true; }
            @Override public boolean isCredentialsNonExpired() { return true; }
            @Override public boolean isAccountNonLocked() { return true; }
            @Override public boolean isAccountNonExpired() { return true; }
            @Override public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() {
                return java.util.Collections.emptyList();
            }
            @Override public String getPassword() { return "123"; }
        };

        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    @Test
    void getClaim_ShouldExtractExpiration() {
        String token = jwtService.getToken(userDetails);
        Date expiration = jwtService.getClaim(token, claims -> claims.getExpiration());

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    
}
