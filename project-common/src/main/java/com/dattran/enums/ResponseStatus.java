package com.dattran.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    EMAIL_OR_USERNAME_EXISTED(405, "Email or username is existed."),
    ROLE_NOT_FOUND(404, "Role not found."),
    UNAUTHENTICATED(401, "Unauthenticated"),
    ACCOUNT_NOT_FOUND(404, "Account not found"),
    USERNAME_OR_EMAIL_EXISTED(500, "Username or email existed"),
    PASSWORD_NOT_MATCH(500, "Password not match"),
    ACCOUNT_NOT_ACTIVATED(500, "Account not activated"),
    OLD_PASSWORD_INCORRECT(500, "Old password incorrect"),
    SEND_OTP_FAILED(500, "Send OTP failed."),
    CUSTOMER_NOT_FOUND(500, "Customer not found."),
    UNAUTHORIZED(401, "Unauthorized"),
    SEND_EMAIL_FAILED(500, "Send Email failed."),
    FORBIDDEN(403, "Forbidden."),
    BRAND_ALREADY_EXIST(500, "Brand is already existed."),
    CANNOT_UPLOAD_FILE(500, "Cannot upload file."),
    NO_FILES_FOUND(500, "No files found."),;
    private final int code;
    private final String message;
}
