package com.dattran.identity_service.app.responses;

import com.dattran.identity_service.domain.entities.Role;
import com.dattran.identity_service.domain.enums.AccountState;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {
    String id;

    String username;

    String email;

    AccountState accountState;

    Set<Role> roles;
}
