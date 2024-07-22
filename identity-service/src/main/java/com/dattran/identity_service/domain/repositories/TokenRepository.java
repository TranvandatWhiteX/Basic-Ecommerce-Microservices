package com.dattran.identity_service.domain.repositories;

import com.dattran.identity_service.domain.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.config.annotation.web.oauth2.login.TokenEndpointDsl;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByAccessTokenId(String accessTokenId);
    List<Token> findByAccountId(String accountId);
}
