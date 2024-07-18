package com.dattran.identity_service.domain.services;

import com.dattran.identity_service.domain.entities.Account;
import com.dattran.identity_service.domain.entities.Role;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class JwtService {
    @Value("${jwt.expiration}")
    @NonFinal
    long EXPIRATION_AC_KEY;

    @Value("${jwt.secret-key}")
    @NonFinal
    String ACCESS_KEY;

    @Value("${jwt.refresh-key}")
    @NonFinal
    String REFRESH_KEY;

    @Value("${jwt.expiration-rf-key}")
    @NonFinal
    long EXPIRATION_RF_KEY;

    public String generateRefreshToken(Account account) {
        return generateToken(account, Optional.empty(), EXPIRATION_RF_KEY, REFRESH_KEY);
    }

    public String generateAccessToken(Account account) {
        List<String> roles = account.getRoles().stream().map(Role::getName).toList();
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", roles);
        claims.put("account_state", account.getAccountState().toString());
        claims.put("email", account.getEmail());
        return generateToken(account, Optional.of(claims), EXPIRATION_AC_KEY, ACCESS_KEY);
    }

    public String generateToken(UserDetails userDetails, Optional<Map<String, Object>> claims, long expiration, String key) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS384);
        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(userDetails.getUsername())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(expiration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString());
        // Additional claims
        claims.ifPresent(stringObjectMap -> stringObjectMap.forEach(claimsBuilder::claim));
        JWTClaimsSet jwtClaimsSet = claimsBuilder.build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(key.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public JWTClaimsSet getAllClaimsFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return claims;
        } catch (ParseException e) {
            log.error("Cannot extract token", e);
            throw new RuntimeException(e);
        }
    }

//    public boolean verifyToken(String token, UserDetails userDetails) {
//        JWSVerifier verifier = new MACVerifier();
//    }
}
