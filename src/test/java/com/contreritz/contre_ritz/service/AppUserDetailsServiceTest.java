package com.contreritz.contre_ritz.service;

import com.contreritz.contre_ritz.model.User;
import com.contreritz.contre_ritz.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppUserDetailsService service;

    @Test
    void loadUserByUsername_doitRenvoyerLUtilisateurTrouveParEmail() {
        // 1. Given : l'email existe en base
        User user = new User();
        user.setEmail("jean@test.fr");
        user.setPassword("$2a$10$hashBcrypt");
        when(userRepository.findByEmail("jean@test.fr")).thenReturn(Optional.of(user));

        // 2. When : Spring Security cherche l'utilisateur
        UserDetails resultat = service.loadUserByUsername("jean@test.fr");

        // 3. Then : on récupère bien le compte, avec son mot de passe haché et son rôle
        assertEquals("jean@test.fr", resultat.getUsername());
        assertEquals("$2a$10$hashBcrypt", resultat.getPassword());
        assertEquals("ROLE_USER", resultat.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_doitLeverUneExceptionSiEmailInconnu() {
        // 1. Given : l'email n'existe pas
        when(userRepository.findByEmail("inconnu@test.fr")).thenReturn(Optional.empty());

        // 2. When & Then : la connexion doit être refusée avec l'exception attendue par Spring Security
        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("inconnu@test.fr"));
    }
}
