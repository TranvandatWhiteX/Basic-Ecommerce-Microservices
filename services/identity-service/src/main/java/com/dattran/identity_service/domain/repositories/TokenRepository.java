package com.dattran.identity_service.domain.repositories;

import com.dattran.identity_service.domain.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
}
