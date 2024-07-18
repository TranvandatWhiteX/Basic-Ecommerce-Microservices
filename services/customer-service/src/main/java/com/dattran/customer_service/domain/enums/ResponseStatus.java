package com.dattran.customer_service.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    CUSTOMER_NOT_FOUND(404, "Customer not found.");
    private final int code;
    private final String message;
}
