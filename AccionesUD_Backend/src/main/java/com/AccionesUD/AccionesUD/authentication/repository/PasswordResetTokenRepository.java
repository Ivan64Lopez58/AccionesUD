package com.AccionesUD.AccionesUD.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AccionesUD.AccionesUD.authentication.domain.model.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
Optional<PasswordResetToken> findByToken(String token);
void deleteByEmail(String email);
}