package com.contreritz.contre_ritz.config;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.aura.Aura;

/**
 * Configuration globale de l'application Vaadin : charge le thème sur toutes les pages,
 * y compris celles qui n'utilisent pas {@code MainLayout} (connexion, inscription).
 */
@StyleSheet(Aura.STYLESHEET)
public class AppShell implements AppShellConfigurator {
}