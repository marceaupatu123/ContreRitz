package com.contreritz.contre_ritz.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;

@AnonymousAllowed
@Layout
@StyleSheet("styles.css")
public class MainLayout extends AppLayout {

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        var scroller = new Scroller(createSideNav());
        scroller.setSizeFull();

        var drawerContent = new VerticalLayout(createHeader(), scroller, createFooter());
        drawerContent.setSizeFull();
        drawerContent.setPadding(false);
        drawerContent.setSpacing(false);
        addToDrawer(drawerContent);

    }

    private Component createHeader() {
        var userLogo = new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkECss14Fj_HeYboz4gzvCKx61Xh7iQkP3CJQCr8MBMw&s=10", "Ma Photo de Profil");
        userLogo.setWidth("50px");
        userLogo.setHeight("50px");
        userLogo.getStyle() // C'est pour transformer l'image carrée en un cercle
                .set("border-radius", "50%")
                .set("object-fit", "cover");

        var userName = new Span("[NOM D'UTILISATEUR]");
        userName.getStyle().setFontWeight(Style.FontWeight.BOLD);

        var solde = new Span("[Solde] $");

        var header = new VerticalLayout(userLogo, userName, solde);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        return header;
    }

    private  Component createFooter() {
        Button deconnexionButton = new Button("Déconnexion", new Icon(VaadinIcon.POWER_OFF));
        deconnexionButton.addThemeVariants(ButtonVariant.PRIMARY, ButtonVariant.ERROR);

        var header = new VerticalLayout(deconnexionButton);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        return header;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        MenuConfiguration.getMenuEntries()
                .forEach(entry -> nav.addItem(createSideNavItem(entry)));
        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        var item = new SideNavItem(menuEntry.title(), menuEntry.menuClass());
        item.setMatchNested(true);
        if (menuEntry.icon() != null) {
            item.setPrefixComponent(new Icon(menuEntry.icon()));
        }
        return item;
    }
}