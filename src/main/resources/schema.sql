-- Tables utilisées par Spring Security pour stocker les passkeys (WebAuthn).
-- Elles sont lues et écrites par JdbcUserCredentialRepository et
-- JdbcPublicKeyCredentialUserEntityRepository : ne pas modifier les noms de colonnes.

-- Lien entre un utilisateur et ses passkeys (name = email de l'utilisateur)
CREATE TABLE IF NOT EXISTS user_entities (
    id           VARCHAR(1000) NOT NULL,
    name         VARCHAR(255)  NOT NULL,
    display_name VARCHAR(200),
    PRIMARY KEY (id),
    -- Permet de se rattache automatiquement à la clé email de l'entitée JPA User
    FOREIGN KEY (name) REFERENCES users (email) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Les passkeys eux-mêmes (clé publique, compteur de signatures, etc.)
CREATE TABLE IF NOT EXISTS user_credentials (
    credential_id                VARCHAR(1000) NOT NULL,
    user_entity_user_id          VARCHAR(1000) NOT NULL,
    public_key                   BLOB          NOT NULL,
    signature_count              BIGINT,
    uv_initialized               BOOLEAN,
    backup_eligible              BOOLEAN       NOT NULL,
    authenticator_transports     VARCHAR(1000),
    public_key_credential_type   VARCHAR(100),
    backup_state                 BOOLEAN       NOT NULL,
    attestation_object           BLOB,
    attestation_client_data_json BLOB,
    created                      TIMESTAMP,
    last_used                    TIMESTAMP,
    label                        VARCHAR(1000) NOT NULL,
    PRIMARY KEY (credential_id),
    FOREIGN KEY (user_entity_user_id) REFERENCES user_entities (id) ON DELETE CASCADE
);
