package com.contreritz.contre_ritz.repository;

import com.contreritz.contre_ritz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Dépôt (Repository) Spring Data JPA pour l'accès aux données des utilisateurs.
 * <p>
 * Fournit automatiquement les opérations CRUD standard (sauvegarde, recherche, suppression)
 * sur l'entité {@link User} ainsi que des requêtes personnalisées dérivées.
 * </p>
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Vérifie si un utilisateur possédant cette adresse email existe déjà en base de données.
     *
     * @param email l'adresse email à rechercher
     * @return {@code true} si l'email est déjà enregistré, {@code false} sinon
     */
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}