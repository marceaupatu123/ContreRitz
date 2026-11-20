package com.contreritz.contre_ritz.schema;

import com.contreritz.contre_ritz.model.User;
import com.contreritz.contre_ritz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.web.webauthn.api.AuthenticatorTransport;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.CredentialRecord;
import org.springframework.security.web.webauthn.api.ImmutableCredentialRecord;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCose;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialType;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.management.JdbcPublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.JdbcUserCredentialRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie le schéma de base de données des passkeys ({@code schema.sql}) :
 * les clés étrangères vers la table {@code users} et la suppression en cascade,
 * ainsi que la compatibilité des tables avec le SQL fourni par Spring Security.
 * <p>
 * Le comportement des classes {@code Jdbc...Repository} elles-mêmes appartient à Spring
 * Security et n'est pas testé ici.
 * </p>
 * <p>
 * Utilise une base H2 en mémoire : la vraie base du projet n'est jamais touchée,
 * et chaque test est annulé à la fin.
 * </p>
 */
@DataJpaTest
class PasskeySchemaTest {

    private static final String EMAIL = "jean@test.fr";

    @Autowired // Autowired c'est pour invoquer le Bean car Spring le passe pas
               // automatiquement dans les tests.

    private UserRepository userRepository;

    @Autowired
    private JdbcOperations jdbc;

    private JdbcPublicKeyCredentialUserEntityRepository userEntities;
    private JdbcUserCredentialRepository credentials;

    @BeforeEach
    void setUp() {
        userEntities = new JdbcPublicKeyCredentialUserEntityRepository(jdbc);
        credentials = new JdbcUserCredentialRepository(jdbc);

        // Le compte applicatif doit exister : user_entities.name pointe vers users.email.
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword("$2a$10$hashBcrypt");
        user.setNom("Dupont");
        user.setPrenom("Jean");
        userRepository.saveAndFlush(user); // On fait un flush pour que l'enregistrement soit immédiat.
    }

    private PublicKeyCredentialUserEntity nouvelleEntiteWebAuthn(String email) {
        return ImmutablePublicKeyCredentialUserEntity.builder()
                .id(Bytes.random())
                .name(email)
                .displayName("Jean Dupont")
                .build();
    }

    private CredentialRecord nouveauPasskey(Bytes userEntityId) {
        Instant maintenant = Instant.now().truncatedTo(ChronoUnit.MILLIS); // On ne va pas jusqu'aux nanosecondes mdrr
        return ImmutableCredentialRecord.builder()
                .credentialType(PublicKeyCredentialType.PUBLIC_KEY)
                .credentialId(Bytes.random())
                .userEntityUserId(userEntityId)
                .publicKey(new ImmutablePublicKeyCose(new byte[]{1, 2, 3, 4}))
                .signatureCount(7)
                .uvInitialized(true)
                .backupEligible(true)
                .backupState(false)
                .transports(Set.of(AuthenticatorTransport.INTERNAL))
                .created(maintenant)
                .lastUsed(maintenant)
                .label("Mon portable")
                .build();
    }

    @Test
    void entiteUtilisateur_doitEtreRefuseeSiLEmailNExistePasDansUsers() {
        PublicKeyCredentialUserEntity entite = nouvelleEntiteWebAuthn("inconnu@test.fr");

        // Clé étrangère user_entities.name -> users.email
        assertThrows(DataIntegrityViolationException.class, () -> userEntities.save(entite));
    }

    @Test
    void passkey_doitEtreRelutApresEnregistrement() { // Test de compatibilité du schéma avec le SQL de Spring
        PublicKeyCredentialUserEntity entite = nouvelleEntiteWebAuthn(EMAIL);
        userEntities.save(entite);
        CredentialRecord passkey = nouveauPasskey(entite.getId());

        credentials.save(passkey);

        CredentialRecord relu = credentials.findByCredentialId(passkey.getCredentialId());
        assertNotNull(relu);
        assertEquals(passkey.getCredentialId(), relu.getCredentialId());
        assertEquals(entite.getId(), relu.getUserEntityUserId());
        assertArrayEquals(new byte[]{1, 2, 3, 4}, relu.getPublicKey().getBytes());
        assertEquals(7, relu.getSignatureCount());
        assertEquals("Mon portable", relu.getLabel());
        assertTrue(relu.isUvInitialized());
        assertTrue(relu.isBackupEligible());
        assertFalse(relu.isBackupState());
        assertEquals(Set.of(AuthenticatorTransport.INTERNAL), relu.getTransports());
    }

    @Test
    void passkey_doitEtreRefuseSiLEntiteUtilisateurNExistePas() {
        CredentialRecord orphelin = nouveauPasskey(Bytes.random());

        // Clé étrangère user_credentials.user_entity_user_id -> user_entities.id
        assertThrows(DataIntegrityViolationException.class, () -> credentials.save(orphelin));
    }

    @Test
    void suppressionDuCompte_doitSupprimerSesPasskeys() {
        PublicKeyCredentialUserEntity entite = nouvelleEntiteWebAuthn(EMAIL);
        userEntities.save(entite);
        CredentialRecord passkey = nouveauPasskey(entite.getId());
        credentials.save(passkey);

        // ON DELETE CASCADE : users -> user_entities -> user_credentials
        jdbc.update("DELETE FROM users WHERE email = ?", EMAIL);

        assertNull(userEntities.findByUsername(EMAIL));
        assertNull(credentials.findByCredentialId(passkey.getCredentialId()));
    }
}
