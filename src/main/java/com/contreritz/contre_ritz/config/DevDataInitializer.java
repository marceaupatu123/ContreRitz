package com.contreritz.contre_ritz.config;

import com.contreritz.contre_ritz.repository.UserRepository;
import com.contreritz.contre_ritz.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Crée un compte de test au démarrage de l'application.
 * <p>
 * Actif uniquement avec le profil Spring {@code dev}. Ne doit jamais
 * être exécuté en production.
 * </p>
 */
@Configuration
@Profile("dev")
public class DevDataInitializer {

    /**
     * Crée l'utilisateur {@code alice@test.fr} s'il n'existe pas déjà.
     *
     * @param userService service d'inscription (hache le mot de passe)
     * @param repo        dépôt utilisé pour vérifier l'existence du compte
     * @return le runner exécuté au démarrage
     */
    @Bean
    CommandLineRunner initUsers(UserService userService, UserRepository repo) {
        return args -> {
            if (!repo.existsByEmail("maeva@test.fr")) {
                userService.inscrire("maeva@test.fr", "test", "Martin Fontaine", "Maeva");
            }
        };
    }
}