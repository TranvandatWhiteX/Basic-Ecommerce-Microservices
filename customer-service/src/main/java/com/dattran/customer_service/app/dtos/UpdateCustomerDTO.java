package com.dattran.customer_service.app.dtos;

import com.dattran.customer_service.domain.entities.Address;
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
public class UpdateCustomerDTO {
    @NotNull(message = "Full name must be not null.")
    String fullName;

    @NotNull(message = "Dob must be not null")
    LocalDate dob;

    @NotNull(message = "Address must be not null.")
    Address address;
}
