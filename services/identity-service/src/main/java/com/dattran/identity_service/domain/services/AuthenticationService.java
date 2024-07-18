package com.dattran.identity_service.domain.services;

import com.dattran.identity_service.app.dtos.AuthenticationDTO;
import com.dattran.identity_service.app.dtos.IntrospectDTO;
import com.dattran.identity_service.app.responses.AuthenticationResponse;
import com.dattran.identity_service.app.responses.IntrospectResponse;
import com.dattran.identity_service.domain.entities.Account;
import com.dattran.identity_service.domain.entities.Token;
import com.dattran.identity_service.domain.enums.AccountState;
import com.dattran.identity_service.domain.enums.ResponseStatus;
import com.dattran.identity_service.domain.enums.TokenType;
import com.dattran.identity_service.domain.exceptions.AppException;
import com.dattran.identity_service.domain.repositories.AccountRepository;
import com.dattran.identity_service.domain.repositories.TokenRepository;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    TokenRepository tokenRepository;

    public AuthenticationResponse login(AuthenticationDTO authenticationDTO) {
        Account account = accountRepository.findByEmail(authenticationDTO.getEmail())
                .orElseThrow(()->new AppException(ResponseStatus.ACCOUNT_NOT_FOUND));
        // Check account
        if (!passwordEncoder.matches(authenticationDTO.getPassword(), account.getPassword())) {
            throw new AppException(ResponseStatus.PASSWORD_NOT_MATCH);
        }
        if (account.getAccountState() != AccountState.ACTIVE) {
            throw new AppException(ResponseStatus.ACCOUNT_NOT_ACTIVATED);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword(), account.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        // Generate tokens
        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);
        // Save token to db
        saveToken(account, accessToken, refreshToken, TokenType.BEARER);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .build();
    }

    public IntrospectResponse introspect(IntrospectDTO introspectDTO) {
        return null;
    }

    private void saveToken(Account account, String accessToken, String refreshToken, TokenType tokenType) {
        JWTClaimsSet accessTokenClaims = jwtService.getAllClaimsFromToken(accessToken);
        JWTClaimsSet refreshTokenClaims = jwtService.getAllClaimsFromToken(refreshToken);
        Token token = Token.builder()
                .accessTokenId(accessTokenClaims.getJWTID())
                .refreshTokenId(refreshTokenClaims.getJWTID())
                .accessTokenExpiration(accessTokenClaims.getExpirationTime())
                .refreshTokenExpiration(refreshTokenClaims.getExpirationTime())
                .isExpired(false)
                .isRevoked(false)
                .account(account)
                .tokenType(tokenType)
                .build();
        tokenRepository.save(token);
    }
}
