package com.AccionesUD.AccionesUD.utilities.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private JavaMailSender mailSender;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailService(mailSender);
        // Simulamos la inyección del valor desde @Value
        setFromEmail(emailService, "noreply@accionesud.com");
    }

    @Test
    void sendOtpEmail_SendsCorrectMessage() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.sendOtpEmail("user@example.com", "123456");

        verify(mailSender).send(captor.capture());
        SimpleMailMessage sent = captor.getValue();

        assertEquals("noreply@accionesud.com", sent.getFrom());
        assertEquals("user@example.com", sent.getTo()[0]);
        assertEquals("Tu código OTP", sent.getSubject());
        assertEquals("Tu código de verificación es: 123456", sent.getText());
    }

    @Test
    void sendEmail_SendsCorrectMessage() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.sendEmail("other@example.com", "Asunto de prueba", "Cuerpo del mensaje");

        verify(mailSender).send(captor.capture());
        SimpleMailMessage sent = captor.getValue();

        assertEquals("noreply@accionesud.com", sent.getFrom());
        assertEquals("other@example.com", sent.getTo()[0]);
        assertEquals("Asunto de prueba", sent.getSubject());
        assertEquals("Cuerpo del mensaje", sent.getText());
    }

    // Método auxiliar para inyectar el valor simulado de @Value
    private void setFromEmail(EmailService emailService, String fromEmail) {
        try {
            var field = EmailService.class.getDeclaredField("fromEmail");
            field.setAccessible(true);
            field.set(emailService, fromEmail);
        } catch (Exception e) {
            throw new RuntimeException("Error al inyectar el campo fromEmail", e);
        }
    }
}
