package com.dattran.identity_service.domain.repositories;

import com.dattran.identity_service.domain.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);
    boolean existsByUsernameOrEmail(String username, String email);
}
