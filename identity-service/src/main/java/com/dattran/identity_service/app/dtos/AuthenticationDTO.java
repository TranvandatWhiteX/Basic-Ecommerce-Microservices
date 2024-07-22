package com.dattran.identity_service.app.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationDTO {
    @Email
    @NotBlank(message = "Email must not be blank")
    @NotNull(message = "Email must not be null")
    String email;

    @NotNull(message = "Password must not be null")
    @NotBlank(message = "Password must not be blank")
    String password;
}
