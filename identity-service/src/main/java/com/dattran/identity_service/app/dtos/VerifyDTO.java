package com.dattran.identity_service.app.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyDTO {
    @NotNull(message = "Account id must be not null.")
    String accountId;

    @NotNull(message = "Otp must be not null.")
    String otp;
}
