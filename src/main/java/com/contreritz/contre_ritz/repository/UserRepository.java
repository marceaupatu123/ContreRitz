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

    /**
     * Recherche un utilisateur à partir de son adresse email.
     * <p>
     * Utilisée par {@code AppUserDetailsService} pour retrouver le compte d'une personne
     * qui se connecte, l'email servant d'identifiant de connexion.
     * </p>
     *
     * @param email l'adresse email à rechercher
     * @return un {@link Optional} contenant l'utilisateur, ou vide si aucun compte n'utilise cet email
     */
    Optional<User> findByEmail(String email);
}