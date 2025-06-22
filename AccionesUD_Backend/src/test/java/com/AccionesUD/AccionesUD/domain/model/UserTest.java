package com.AccionesUD.AccionesUD.domain.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGettersAndSetters() {
        User user = new User();

        Double id = 12345678.0;
        String username = "usuario@ejemplo.com";
        String password = "claveSegura123";
        String firstname = "Juan";
        String lastname = "Pérez";
        Double phone = 3001234567.0;
        String address = "Calle Falsa 123";
        Role role = Role.ADMIN;
        boolean otpEnabled = true;
        int dailyOrderLimit = 10;

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(role);
        user.setOtpEnabled(otpEnabled);
        user.setDailyOrderLimit(dailyOrderLimit);

        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(firstname, user.getFirstname());
        assertEquals(lastname, user.getLastname());
        assertEquals(phone, user.getPhone());
        assertEquals(address, user.getAddress());
        assertEquals(role, user.getRole());
        assertTrue(user.isOtpEnabled());
        assertEquals(dailyOrderLimit, user.getDailyOrderLimit());
    }

    @Test
    void testAllArgsConstructor() {
        Double id = 98765432.0;
        String username = "admin@ud.edu.co";
        String password = "admin123";
        String firstname = "Admin";
        String lastname = "Master";
        Double phone = 3019998888.0;
        String address = "Carrera 45";
        Role role = Role.USER;
        boolean otpEnabled = false;
        int dailyOrderLimit = 5;

        User user = new User(
                id, username, lastname, firstname,
                password, phone, address, role,
                otpEnabled, dailyOrderLimit
        );

        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(firstname, user.getFirstname());
        assertEquals(lastname, user.getLastname());
        assertEquals(phone, user.getPhone());
        assertEquals(address, user.getAddress());
        assertEquals(role, user.getRole());
        assertFalse(user.isOtpEnabled());
        assertEquals(dailyOrderLimit, user.getDailyOrderLimit());
    }

    @Test
    void testUserDetailsInterfaceMethods() {
        Role role = Role.USER;
        User user = User.builder()
                .id(111.0)
                .username("test@correo.com")
                .password("clave")
                .firstname("Test")
                .lastname("User")
                .phone(1234567890.0)
                .address("Mi dirección")
                .role(role)
                .otpEnabled(false)
                .dailyOrderLimit(3)
                .build();

        // Authorities
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(role.name())));

        // Métodos de UserDetails
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }
}
