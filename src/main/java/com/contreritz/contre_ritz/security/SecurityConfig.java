package com.contreritz.contre_ritz.security;

import com.contreritz.contre_ritz.view.LoginView;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.webauthn.management.JdbcPublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.JdbcUserCredentialRepository;
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextHolderStrategy securityContextHolderStrategy) {

        // Un peu compliqué a expliqué et j'ai pas tout compris mais en gros faut le laisser pour le WebAuthN.
        SecurityContextHolder.setContextHolderStrategy(securityContextHolderStrategy);

        // Autorise mes routes
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**")
                .permitAll() // TODO: Ne pas garder ça en production évidemment...
                .requestMatchers("/webauthn/**")
                .authenticated());

        // C'est juste pour le /h2-console
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // Setup le login form
        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> configurer.loginView(LoginView.class));

        // Active le passkey
        http.webAuthn(w -> w
                .rpName("Contre Ritz")
                .rpId("localhost")
                .allowedOrigins("http://localhost:8080"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Stocke les passkeys (clés publiques) dans la table {@code user_credentials}.
     * Spring Security détecte ce bean et l'utilise à la place du stockage en mémoire.
     */
    @Bean
    public UserCredentialRepository userCredentialRepository(JdbcOperations jdbc) {
        return new JdbcUserCredentialRepository(jdbc);
    }

    /**
     * Stocke le lien utilisateur / passkeys dans la table {@code user_entities}
     * (le nom correspond à l'email de l'utilisateur).
     */
    @Bean
    public PublicKeyCredentialUserEntityRepository publicKeyCredentialUserEntityRepository(JdbcOperations jdbc) {
        return new JdbcPublicKeyCredentialUserEntityRepository(jdbc);
    }

}