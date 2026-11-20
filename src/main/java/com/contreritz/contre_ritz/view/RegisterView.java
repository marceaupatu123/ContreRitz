package com.contreritz.contre_ritz.view;

import com.contreritz.contre_ritz.dto.RegisterForm;
import com.contreritz.contre_ritz.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("register")
@PageTitle("Inscription")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    public RegisterView(UserService userService) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        TextField prenom = new TextField("Prénom");
        TextField nom = new TextField("Nom");
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Mot de passe");

        // Le Binder relie chaque champ du formulaire à une propriété de RegisterForm
        // il vérifie les règles ci-dessous, affiche les erreurs sous les champs,
        // et ne remplit RegisterForm que si tout est valide (voir writeBeanIfValid plus bas)
        Binder<RegisterForm> binder = new Binder<>(RegisterForm.class);
        binder
                .forField(prenom)
                .asRequired("Prénom obligatoire")
                .bind(RegisterForm::getPrenom, RegisterForm::setPrenom);

        binder
                .forField(nom)
                .asRequired("Nom obligatoire")
                .bind(RegisterForm::getNom, RegisterForm::setNom);

        binder
                .forField(email)
                .asRequired("Email obligatoire")
                .withValidator(new EmailValidator("Format d'email invalide"))
                .withValidator(v -> !userService.isEmailUsed(v), "Cet email est déjà utilisé")
                .bind(RegisterForm::getEmail, RegisterForm::setEmail);

        binder
                .forField(password)
                .asRequired("Mot de passe obligatoire")
                .withValidator(v -> v.length() >= 8, "8 caractères minimum")
                .bind(RegisterForm::getPassword, RegisterForm::setPassword);

        Button valider = new Button("S'inscrire", e -> {
            RegisterForm form = new RegisterForm();
            if (binder.writeBeanIfValid(form)) {
                userService.inscrire(form.getEmail(), form.getPassword(), form.getNom(), form.getPrenom());
                Notification.show("Compte créé, vous pouvez vous connecter");
                UI.getCurrent().navigate("login");
            }
        });
        valider.addThemeVariants(ButtonVariant.PRIMARY);
        valider.addClassName(LumoUtility.Width.FULL);

        // Une seule colonne, pour que tous les champs aient la même largeur
        FormLayout formulaire = new FormLayout(prenom, nom, email, password);
        formulaire.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        // Le titre
        H1 titre = new H1("Inscription");
        titre.addClassName(LumoUtility.TextAlignment.CENTER);

        add(creerCarte(titre, formulaire, valider));
    }

    /**
     * Regroupe le titre, les champs et le bouton dans une carte centrée
     * (même aspect que le {@code LoginForm} de la page de connexion).
     *
     * @param titre      le titre de la page
     * @param formulaire les champs du formulaire
     * @param valider    le bouton d'envoi
     * @return la carte prête à être ajoutée à la page
     */
    private VerticalLayout creerCarte(H1 titre, FormLayout formulaire, Button valider) {
        VerticalLayout carte = new VerticalLayout(titre, formulaire, valider);
        carte.addClassNames(
                LumoUtility.MaxWidth.SCREEN_SMALL,
                LumoUtility.Padding.LARGE,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST_10,
                LumoUtility.BorderRadius.LARGE);
        return carte;
    }
}