package com.contreritz.contre_ritz.service;

import com.contreritz.contre_ritz.model.User;
import com.contreritz.contre_ritz.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void inscrire_doitHasherLeMotDePasseEtSauvegarderLesBonnesInfos() {
        // 1. Given : l'email est libre et l'encodeur transforme le mot de passe
        when(userRepository.existsByEmail("jean@test.fr")).thenReturn(false);
        when(passwordEncoder.encode("MonMotDePasseEnClair")).thenReturn("$2a$10$hashBcrypt");

        // 2. When : on lance l'inscription
        userService.inscrire("jean@test.fr", "MonMotDePasseEnClair", "Dupont", "Jean");

        // 3. Then : on "attrape" l'objet User exactement tel qu'il a été envoyé à la base de données
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User userEnvoyeEnBase = userCaptor.getValue();

        // Vérifications critiques (anti-régression et sécurité)
        assertEquals("jean@test.fr", userEnvoyeEnBase.getEmail());
        assertEquals("Dupont", userEnvoyeEnBase.getNom());
        assertEquals("Jean", userEnvoyeEnBase.getPrenom());

        // Garantie de sécurité : le mot de passe en clair ne part JAMAIS en base
        assertNotEquals("MonMotDePasseEnClair", userEnvoyeEnBase.getPassword());
        assertEquals("$2a$10$hashBcrypt", userEnvoyeEnBase.getPassword());
    }

    @Test
    void inscrire_doitBloquerUnEmailDejaExistantSansToucherALaBase() {
        // 1. Given : l'email est déjà pris
        when(userRepository.existsByEmail("doublon@test.fr")).thenReturn(true);

        // 2. When & Then : l'inscription doit lever l'exception prévue
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.inscrire("doublon@test.fr", "secret123", "Martin", "Paul"));

        assertEquals("Cet email est déjà utilisé.", exception.getMessage());

        // Garantie métier : si l'email existe, on ne doit ni hacher de mot de passe, ni tenter un save()
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}