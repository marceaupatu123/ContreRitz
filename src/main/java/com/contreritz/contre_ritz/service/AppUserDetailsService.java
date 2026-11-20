package com.contreritz.contre_ritz.service;

import com.contreritz.contre_ritz.repository.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service chargé de retrouver un utilisateur pour Spring Security lors de l'authentification.
 * <p>
 * Lorsqu'une personne se connecte (formulaire de login, passkey, etc.), Spring Security
 * appelle {@link #loadUserByUsername(String)} pour récupérer le compte correspondant,
 * puis compare le mot de passe saisi avec le hash stocké en base.
 * </p>
 * <p>
 * <strong>Cette classe n'est jamais appelée directement dans le code de l'application :</strong>
 * Spring la détecte automatiquement (c'est un {@code @Service} qui implémente
 * {@link UserDetailsService}) et l'utilise lui-même. Pour rechercher un utilisateur depuis
 * le code métier ou une vue, utiliser {@link UserService}.
 * </p>
 * <p>
 * Cette classe ne gère que la <strong>recherche</strong> de l'utilisateur pour la sécurité.
 * La logique métier (inscription, etc.) reste dans {@link UserService}.
 * </p>
 */
@Service
@NullMarked
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    /**
     * Construit le service avec ses dépendances injectées par Spring.
     *
     * @param repo le dépôt permettant de rechercher les utilisateurs en base
     */
    public AppUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * Recherche un utilisateur à partir de son adresse email.
     * <p>
     * Méthode appelée automatiquement par Spring Security pendant l'authentification.
     * Elle n'est pas destinée à être appelée directement dans le code métier.
     * L'adresse email sert ici d'identifiant de connexion (« username »).
     * </p>
     *
     * @param email l'adresse email saisie par la personne qui se connecte
     * @return l'utilisateur correspondant, qui implémente {@link UserDetails}
     * @throws UsernameNotFoundException si aucun utilisateur n'est associé à cette adresse email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}