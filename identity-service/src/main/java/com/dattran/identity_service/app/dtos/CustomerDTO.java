package com.dattran.identity_service.app.dtos;

import com.dattran.identity_service.domain.entities.Address;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {
    @NotNull(message = "Full name must be not null.")
    String fullName;

    LocalDate dob;

    @NotNull(message = "User id must be not null.")
    String userId;

    @NotNull(message = "Address must be not null.")
    Address address;
}
