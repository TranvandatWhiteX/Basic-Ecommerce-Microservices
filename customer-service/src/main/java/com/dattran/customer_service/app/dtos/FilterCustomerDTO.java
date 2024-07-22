package com.dattran.customer_service.app.dtos;

import com.dattran.customer_service.domain.enums.CustomerState;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterCustomerDTO {
    String fullName;
    String customerState;
    Instant startSearch;
    Instant endSearch;
    String province;
    String district;
    String ward;
    String street;
    String detail;

    @AssertTrue(message = "Invalid date time")
    private boolean validateSearchTime() {
        return !startSearch.isAfter(endSearch);
    }
}
