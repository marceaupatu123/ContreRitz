package com.contreritz.contre_ritz.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("")
@Menu(title = "Accueil", icon = "vaadin:home")
@PermitAll
class HomeView extends VerticalLayout {

    HomeView() {
        add("Voici la page d'accueil !");

    }
}
