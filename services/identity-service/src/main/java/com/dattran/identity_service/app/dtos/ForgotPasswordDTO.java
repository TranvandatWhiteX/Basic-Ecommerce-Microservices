package com.dattran.identity_service.app.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.AssertTrue;
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
public class ForgotPasswordDTO {
    @Email
    @NotBlank(message = "Email must not be blank")
    @NotNull(message = "Email must not be null")
    String email;

    @NotNull(message = "Password must not be null")
    @NotBlank(message = "Password must not be blank")
    String password;

    @NotNull(message = "Confirm password must not be null")
    @NotBlank(message = "Confirm password must not be blank")
    String confirmPassword;

    @AssertTrue(message = "Password not match")
    private boolean isPasswordMatch() {
        return password.equals(confirmPassword);
    }
}
