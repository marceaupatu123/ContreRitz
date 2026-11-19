package com.contreritz.contre_ritz.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.web.csrf.CsrfToken;

/**
 * Page de connexion de l'application, accessible à l'URL {@code /login}.
 * <p>
 * Elle propose deux moyens de se connecter :
 * </p>
 * <ul>
 *     <li>le formulaire classique (email et mot de passe), traité par Spring Security ;</li>
 *     <li>un bouton « Se connecter avec un passkey », qui s'appuie sur le script
 *     {@code webauthn.js} et sur les endpoints WebAuthn de Spring Security.</li>
 * </ul>
 * <p>
 * La vue est déclarée comme page de connexion dans {@code SecurityConfig}
 * et doit rester accessible sans être connecté ({@link AnonymousAllowed}).
 * </p>
 */
@Route("login")
@PageTitle("Connexion")
@AnonymousAllowed
@JsModule("./webauthn.js") // Script d'accès à l'API WebAuthn du navigateur (passkeys)
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    /**
     * Formulaire de connexion fourni par Vaadin (identifiant et mot de passe).
     * Le champ « identifiant » correspond à l'adresse email de l'utilisateur.
     */
    private final LoginForm loginForm = new LoginForm();

    /**
     * Construit la page : centre le contenu et ajoute le titre,
     * le formulaire classique et le bouton de connexion par passkey.
     */
    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Les identifiants sont envoyés à Spring Security, qui les vérifie
        loginForm.setAction("login");

        Button passkeyButton = new Button("Se connecter avec un passkey", e -> signInWithPasskey());

        add(new H1("Connexion"), loginForm, passkeyButton);
    }

    /**
     * Lance la connexion par passkey.
     * <p>
     * Récupère le jeton CSRF de la requête en cours, puis le transmet à la fonction
     * JavaScript {@code window.passkeys.authenticate} (définie dans {@code webauthn.js}).
     * Ce script demande les options au serveur, fait signer le défi par le passkey
     * de l'utilisateur, puis renvoie l'URL de redirection.
     * </p>
     * <ul>
     *     <li>En cas de succès : le navigateur est redirigé vers l'URL reçue.</li>
     *     <li>En cas d'échec (passkey refusé, opération annulée, etc.) : une notification
     *     d'erreur est affichée.</li>
     * </ul>
     * <p>
     * Le jeton CSRF est nécessaire car les appels du script aux endpoints WebAuthn
     * sont des requêtes {@code POST} protégées par Spring Security.
     * </p>
     */
    private void signInWithPasskey() {
        CsrfToken csrf = (CsrfToken) VaadinServletRequest.getCurrent()
                .getAttribute(CsrfToken.class.getName());

        getElement().executeJs(
                        "return window.passkeys.authenticate({ headerName: $0, token: $1 })",
                        csrf.getHeaderName(), csrf.getToken())
                .then(String.class,
                        redirectUrl -> UI.getCurrent().getPage().setLocation(redirectUrl),
                        error -> Notification.show("Échec de la connexion par passkey : " + error)
                                .addThemeVariants(NotificationVariant.ERROR));
    }

    /**
     * Appelée par Vaadin juste avant l'affichage de la page.
     * <p>
     * Si l'URL contient le paramètre {@code error} (ajouté par Spring Security après
     * un échec de connexion par formulaire), affiche le message d'erreur du formulaire.
     * </p>
     *
     * @param event l'événement de navigation, qui donne accès à l'URL demandée
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}