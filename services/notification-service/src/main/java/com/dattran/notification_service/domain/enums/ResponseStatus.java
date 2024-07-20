package com.dattran.notification_service.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    SEND_EMAIL_FAILED(500, "Send Email failed.");
    private final int code;
    private final String message;
}
