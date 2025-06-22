// === PasswordResetTokenRepositoryTest ===
package com.AccionesUD.AccionesUD.repository;

import com.AccionesUD.AccionesUD.domain.model.auth.PasswordResetToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PasswordResetTokenRepositoryTest {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Test
    void testFindByToken() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("abc123");
        token.setEmail("correo@ejemplo.com");
        tokenRepository.save(token);

        Optional<PasswordResetToken> result = tokenRepository.findByToken("abc123");
        assertTrue(result.isPresent());
        assertEquals("correo@ejemplo.com", result.get().getEmail());
    }

    @Test
    void testDeleteByEmail() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("toDelete");
        token.setEmail("delete@correo.com");
        tokenRepository.save(token);

        tokenRepository.deleteByEmail("delete@correo.com");

        Optional<PasswordResetToken> result = tokenRepository.findByToken("toDelete");
        assertTrue(result.isEmpty());
    }
}