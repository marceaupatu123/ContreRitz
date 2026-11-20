package com.contreritz.contre_ritz.dto;

/**
 * Objet de transfert de données (DTO) portant les valeurs saisies dans le formulaire d'inscription.
 * <p>
 * Il sert de cible au {@code Binder} de {@code RegisterView} : une fois les champs validés,
 * leurs valeurs y sont copiées, puis transmises à {@code UserService#inscrire}.
 * </p>
 * <p>
 * Cette classe n'est pas une entité JPA et n'est jamais enregistrée en base. Le mot de passe
 * y est conservé <strong>en clair</strong> le temps de l'inscription : il est haché par
 * {@code UserService} avant d'être sauvegardé dans {@code User}.
 * </p>
 */
public class RegisterForm {

    /** Prénom saisi par le visiteur. */
    private String prenom;

    /** Nom de famille saisi par le visiteur. */
    private String nom;

    /** Adresse email saisie, utilisée comme identifiant de connexion. */
    private String email;

    /** Mot de passe saisi, en clair (haché plus tard par {@code UserService}). */
    private String password;

    /**
     * Retourne le prénom saisi.
     *
     * @return le prénom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom saisi.
     *
     * @param prenom le prénom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourne le nom de famille saisi.
     *
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de famille saisi.
     *
     * @param nom le nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne l'adresse email saisie.
     *
     * @return l'email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email saisie.
     *
     * @param email l'email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne le mot de passe saisi, en clair.
     *
     * @return le mot de passe non haché
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe saisi, en clair.
     *
     * @param password le mot de passe non haché
     */
    public void setPassword(String password) {
        this.password = password;
    }
}