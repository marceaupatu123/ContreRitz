// Oui, ce fichier n'a rien à voir avec la sécurité mais
// pour une raison où un autre je suis obligé de faire ça
// pour me connecter à la DB avec IntelliJ et je ne savais pas
// où le mettre :(

package com.contreritz.contre_ritz.security;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class H2ServerConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
}