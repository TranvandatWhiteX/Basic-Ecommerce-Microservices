package com.dattran.identity_service.domain.entities;

import com.dattran.identity_service.domain.enums.AccountState;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Account extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username", unique = true)
    String username;

    String password;

    @Column(name = "email", unique = true)
    String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_state")
    AccountState accountState;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_accounts_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "account")
    List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountState == AccountState.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountState == AccountState.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // or add logic to handle credential expiration
    }

    @Override
    public boolean isEnabled() {
        return accountState == AccountState.ACTIVE;
    }
}
