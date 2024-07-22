package com.dattran.identity_service.app.dtos;

import com.dattran.identity_service.domain.entities.Address;
import com.dattran.identity_service.domain.enums.AccountRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO {
    @NotNull(message = "User name must not be null.")
    @NotEmpty(message = "User name must not be empty")
    String username;

    @NotNull(message = "Password must not be null.")
    @NotEmpty(message = "Password must")
    String password;

    @NotNull(message = "Email must be not null.")
    @NotEmpty(message = "Email must be not empty.")
    String email;

    List<AccountRole> roles;

    @NotNull(message = "Full name must not be null.")
    @NotEmpty(message = "Full name must not be empty.")
    String fullName;

    Integer age;

    LocalDate dob;

    @NotNull(message = "Address must not be null.")
    Address address;

    @AssertTrue(message = "Role must have at least 1.")
    private boolean validateRoles() {
        return !roles.isEmpty();
    }
}
