package com.contreritz.contre_ritz.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("home")
@Menu(title = "Home", icon = "vaadin:home")
@AnonymousAllowed
class HomeView extends VerticalLayout {

    HomeView() {
        add("Voici la page d'accueil !");
    }
}