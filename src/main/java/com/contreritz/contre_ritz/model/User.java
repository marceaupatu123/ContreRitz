package com.contreritz.contre_ritz.model;

import org.jspecify.annotations.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Entité JPA représentant un utilisateur de l'application en base de données.
 * <p>
 * Mappée sur la table {@code users} (le nom {@code user} étant un mot-clé réservé en SQL).
 * Implémente {@link UserDetails} pour s'intégrer directement au mécanisme d'authentification
 * de Spring Security.
 * </p>
 * <p>
 * Annotée {@link NullMarked} : tous les paramètres et valeurs de retour sont considérés
 * non nuls par défaut, sauf mention explicite via {@link Nullable}.
 * </p>
 */
@SuppressWarnings("unused")
@Entity
@NullMarked
@Table(name = "users")
public class User implements UserDetails {

    /**
     * Identifiant unique de l'utilisateur, généré automatiquement par la base de données.
     * Est {@code null} tant que l'entité n'a pas été persistée.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    /**
     * Adresse email unique servant d'identifiant de connexion.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Column(unique = true, nullable = false)
    private String email = "";

    /**
     * Mot de passe de l'utilisateur stocké sous forme hachée (BCrypt).
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Column(nullable = false)
    private String password = "";

    /**
     * Nom de famille de l'utilisateur.
     */
    @NotBlank(message = "Le nom est obligatoire")
    private String nom = "";

    /**
     * Prénom de l'utilisateur.
     */
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom = "";

    /**
     * Constructeur par défaut sans arguments requis par la spécification JPA/Hibernate.
     */
    public User() {
    }

    /**
     * Retourne l'identifiant unique de l'utilisateur encapsulé dans un {@link Optional}.
     *
     * @return un {@link Optional} contenant l'identifiant généré en base
     * @throws AssertionError si l'entité n'a pas encore été persistée ({@code id == null})
     *                        et que les assertions JVM ({@code -ea}) sont activées
     */
    public Optional<Long> getId() {
        assert id != null;
        return id.describeConstable();
    }

    /**
     * Définit l'identifiant unique de l'utilisateur.
     *
     * @param id le nouvel identifiant
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne l'adresse email de l'utilisateur.
     *
     * @return l'adresse email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email de l'utilisateur.
     *
     * @param email la nouvelle adresse email (doit être unique)
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne les autorités (rôles) accordées à l'utilisateur.
     * <p>
     * Méthode imposée par l'interface {@link UserDetails} et appelée par Spring Security
     * pour contrôler les accès (par exemple {@code @RolesAllowed("USER")}).
     * Pour l'instant, tous les utilisateurs reçoivent le rôle {@code USER}.
     * </p>
     * <p>
     * Le préfixe {@code ROLE_} est obligatoire pour que les vérifications par rôle fonctionnent.
     * </p>
     *
     * @return une collection non nulle contenant l'autorité {@code ROLE_USER}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * Retourne le mot de passe haché de l'utilisateur.
     *
     * @return l'empreinte (hash) BCrypt du mot de passe
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retourne l'identifiant de connexion de l'utilisateur (son adresse email).
     * <p>
     * Méthode réservée à Spring Security (contrat {@link UserDetails}).
     * </p>
     *
     * @return l'adresse email
     * @deprecated À usage exclusif de Spring Security ; préférer {@link #getEmail()} dans le code applicatif.
     */
    @Deprecated
    @Override
    public String getUsername() {
        return getEmail();
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     * <p>
     * Doit impérativement recevoir un mot de passe déjà haché (via {@code PasswordEncoder})
     * et jamais un mot de passe en clair.
     * </p>
     *
     * @param password le mot de passe haché
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retourne le nom de famille de l'utilisateur.
     *
     * @return le nom de famille
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de famille de l'utilisateur.
     *
     * @param nom le nouveau nom de famille
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le prénom de l'utilisateur.
     *
     * @return le prénom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom de l'utilisateur.
     *
     * @param prenom le nouveau prénom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}