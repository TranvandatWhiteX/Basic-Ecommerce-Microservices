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
import com.dattran.identity_service.domain.utils.SecurityUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    SecurityUtil securityUtil;
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
        var token = introspectDTO.getToken();
        JWTClaimsSet claimsSet = jwtService.getAllClaimsFromToken(token);
        Account account = accountRepository.findByEmail(claimsSet.getSubject())
                .orElseThrow(()->new AppException(ResponseStatus.ACCOUNT_NOT_FOUND));
        IntrospectResponse introspectResponse = new IntrospectResponse();
        try {
            boolean isVerified = jwtService.verifyToken(token, account, false);

            Optional<Token> tokenInDB = tokenRepository.findByAccessTokenId(claimsSet.getJWTID());
            if (tokenInDB.isEmpty()) {
                introspectResponse.setValid(false);
            } else {
                introspectResponse.setValid(isVerified && !tokenInDB.get().isRevoked());
            }
            return introspectResponse;
        } catch (JOSEException | ParseException e) {
            log.error("Error while parsing token: ", e);
            introspectResponse.setValid(false);
            return introspectResponse;
        }
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

    public void logout(HttpServletRequest httpServletRequest) {
        final String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException(ResponseStatus.UNAUTHENTICATED);
        }
        final String authToken = authHeader.substring(7);
        JWTClaimsSet claimsSet = jwtService.getAllClaimsFromToken(authToken);
        Account account = accountRepository.findByEmail(claimsSet.getSubject())
                .orElseThrow(()->new AppException(ResponseStatus.ACCOUNT_NOT_FOUND));
        List<Token> tokens = tokenRepository.findByAccountId(account.getId());
        if (!tokens.isEmpty()) {
            tokens.forEach(token -> {
                if (!token.isExpired()) {
                    token.setExpired(true);
                }
                if (!token.isRevoked()) {
                    token.setRevoked(true);
                }
            });
        }
        tokenRepository.saveAll(tokens);
    }
}
