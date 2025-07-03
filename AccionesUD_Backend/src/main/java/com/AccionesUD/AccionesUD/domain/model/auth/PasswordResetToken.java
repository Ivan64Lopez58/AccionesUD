package com.AccionesUD.AccionesUD.domain.model.auth;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String token;

private String email;

private LocalDateTime expiration;

private boolean used;
}