package com.AccionesUD.AccionesUD.repository;

import com.AccionesUD.AccionesUD.domain.model.Role;
import com.AccionesUD.AccionesUD.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User buildTestUser(Double id, String username) {
    return User.builder()
            .id(id)
            .username(username)
            .password("test123")
            .firstname("Juan")
            .lastname("Pérez")
            .phone(3101234567.0)
            .address("Calle 123")
            .role(Role.USER)
            .otpEnabled(false)
            .dailyOrderLimit(1)
            .build();
}


    @Test
    void findByUsername_existente_retornaUsuario() {
        User user = buildTestUser(1001.0, "test@correo.com");
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("test@correo.com");
        assertTrue(result.isPresent());
        assertEquals("test@correo.com", result.get().getUsername());
    }

    @Test
    void existsByUsername_existente_true() {
        User user = buildTestUser(1002.0, "existente@correo.com");
        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("existente@correo.com");
        assertTrue(exists);
    }
}
