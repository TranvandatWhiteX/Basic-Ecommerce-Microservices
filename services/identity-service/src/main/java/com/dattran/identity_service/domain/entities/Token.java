package com.dattran.identity_service.domain.entities;

import com.dattran.identity_service.domain.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    TokenType tokenType;

    @Column(name = "access_token_id")
    String accessTokenId;

    @Column(name = "refresh_token_id")
    String refreshTokenId;

    @Column(name = "access_token_expiration")
    Date accessTokenExpiration;

    @Column(name = "refresh_token_expiration")
    Date refreshTokenExpiration;

    @Column(name = "is_revoked")
    boolean isRevoked;

//    For Refresh Token
    @Column(name = "is_expired")
    boolean isExpired;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;
}
