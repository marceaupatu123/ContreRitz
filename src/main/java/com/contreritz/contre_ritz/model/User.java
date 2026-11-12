package com.contreritz.contre_ritz.model;

import jakarta.persistence.*;

/**
 * Entité JPA représentant un utilisateur de l'application en base de données.
 * <p>
 * Mappée sur la table {@code users} (le nom {@code user} étant un mot-clé réservé en SQL).
 * </p>
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Identifiant unique de l'utilisateur, généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Adresse email unique servant d'identifiant de connexion. Ne peut pas être nulle.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Mot de passe de l'utilisateur stocké sous forme hachée (BCrypt). Ne peut pas être nul.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Nom de famille de l'utilisateur.
     */
    private String nom;

    /**
     * Prénom de l'utilisateur.
     */
    private String prenom;

    /**
     * Constructeur par défaut sans arguments requis par la spécification JPA/Hibernate.
     */
    public User() {
    }

    /**
     * Retourne l'identifiant unique de l'utilisateur.
     *
     * @return l'identifiant généré en base, ou {@code null} si l'entité n'est pas encore persistée
     */
    public Long getId() {
        return id;
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
     * Retourne le mot de passe haché de l'utilisateur.
     *
     * @return le empreinte (hash) BCrypt du mot de passe
     */
    public String getPassword() {
        return password;
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