package com.contreritz.contre_ritz.service;

import com.contreritz.contre_ritz.model.User;
import com.contreritz.contre_ritz.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service métier responsable de la gestion des utilisateurs.
 * <p>
 * Centralise la logique d'inscription, la vérification d'unicité des identifiants
 * et le chiffrement sécurisé des mots de passe avant persistance.
 * </p>
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construit le service avec ses dépendances injectées par Spring.
     *
     * @param userRepository  le dépôt permettant d'accéder aux données des utilisateurs
     * @param passwordEncoder l'encodeur utilisé pour hacher les mots de passe (BCrypt)
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Inscrit un nouvel utilisateur dans l'application.
     * <p>
     * Vérifie que l'adresse email n'est pas déjà enregistrée, hache le mot de passe
     * en clair via BCrypt, puis sauvegarde l'entité en base de données.
     * </p>
     *
     * @param email       l'adresse email unique de l'utilisateur
     * @param rawPassword le mot de passe en clair saisi par le visiteur
     * @param nom         le nom de famille de l'utilisateur
     * @param prenom      le prénom de l'utilisateur
     * @throws IllegalArgumentException si l'adresse email est déjà associée à un compte existant
     */

    /**
     * Indique si une adresse email est déjà associée à un compte.
     * <p>
     * Permet aux vues (par exemple le formulaire d'inscription) de prévenir l'utilisateur
     * sans accéder directement au repository.
     * </p>
     *
     * @param email l'adresse email à vérifier
     * @return {@code true} si un compte utilise déjà cet email, {@code false} sinon
     */
    public boolean isEmailUsed(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional // Bon ici ça sert un peu à rien car y'a qu'un .save() mais c'est une bonne pratique !
    public void inscrire(String email, String rawPassword, String nom, String prenom) {
        // Vérification anti-doublon
        if (isEmailUsed(email)) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        // Création de l'utilisateur
        User theUser = new User();
        theUser.setEmail(email);
        theUser.setPassword(passwordEncoder.encode(rawPassword));
        theUser.setNom(nom);
        theUser.setPrenom(prenom);

        // Sauvegarde et renvoie l'utilisateur créé
        userRepository.save(theUser);
    }
}