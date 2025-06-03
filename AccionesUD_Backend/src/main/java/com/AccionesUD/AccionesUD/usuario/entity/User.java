package com.AccionesUD.AccionesUD.usuario.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class User implements UserDetails {

    @Id
    @Column(nullable = false, unique = true)
    private Double id; // Cédula de identidad o pasaporte
    
    @Column(nullable = false, unique = true)
    private String username; // Correo electrónico

    private String lastname;

    private String firstname;

    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private Double phone; // Cambiado de Double a String

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // Por defecto será USER

    @Column(nullable = false)
    private boolean otpEnabled = false;

    @Column(nullable = false)
    private int dailyOrderLimit = 1;



    // Métodos obligatorios de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
