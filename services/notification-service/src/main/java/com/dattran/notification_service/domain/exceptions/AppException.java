package com.dattran.notification_service.domain.exceptions;

import com.dattran.notification_service.domain.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException{
    private ResponseStatus responseStatus;
    public AppException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.responseStatus = responseStatus;
    }
}
