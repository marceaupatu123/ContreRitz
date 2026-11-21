package com.contreritz.contre_ritz.view;

import com.contreritz.contre_ritz.model.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@SuppressWarnings("unused") // Utilisée par Vaadin grâce à @Layout.
@Layout
@PermitAll
public class MainLayout extends AppLayout {

    private AuthenticationContext auth;

    public MainLayout(AuthenticationContext auth) {
        setPrimarySection(Section.DRAWER);
        var scroller = new Scroller(createSideNav());
        scroller.setSizeFull();

        var drawerContent = new VerticalLayout(createHeader(auth), scroller, createFooter(auth));
        drawerContent.setSizeFull();
        drawerContent.setPadding(false);
        drawerContent.setSpacing(false);
        addToDrawer(drawerContent);
    }

    private Component createHeader(AuthenticationContext auth) {
        User monUser = auth
                .getAuthenticatedUser(User.class)
                .orElseThrow(() -> new IllegalStateException("Utilisateur non connecté"));

        String displayName = monUser.getDisplayName();

        Avatar userAvatar = new Avatar(displayName);
        userAvatar.addThemeVariants(AvatarVariant.LUMO_XLARGE); // c'est en gros pour dire que mon avatar doit être en L.

        var userName = new Span(displayName);
        userName.addClassNames(LumoUtility.FontWeight.BOLD);

        var solde = new Span("67€"); // TODO: Penser à mettre le vrai solde du User.

        var header = new VerticalLayout(userAvatar, userName, solde);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.addClassNames(LumoUtility.TextAlignment.CENTER);
        return header;
    }

    private Component createFooter(AuthenticationContext auth) {
        Button deconnexionButton = new Button("Déconnexion", new Icon(VaadinIcon.POWER_OFF), event -> auth.logout());
        deconnexionButton.addThemeVariants(ButtonVariant.PRIMARY, ButtonVariant.ERROR);
        deconnexionButton.setWidthFull();

        var header = new VerticalLayout(deconnexionButton);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        return header;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        MenuConfiguration
                .getMenuEntries()
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
