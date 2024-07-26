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
    USERNAME_OR_EMAIL_EXISTED(400, "Username or email existed"),
    PASSWORD_NOT_MATCH(400, "Password not match"),
    ACCOUNT_NOT_ACTIVATED(400, "Account not activated"),
    OLD_PASSWORD_INCORRECT(400, "Old password incorrect"),
    SEND_OTP_FAILED(500, "Send OTP failed."),
    CUSTOMER_NOT_FOUND(400, "Customer not found."),
    UNAUTHORIZED(401, "Unauthorized"),
    SEND_EMAIL_FAILED(500, "Send Email failed."),
    FORBIDDEN(403, "Forbidden."),
    BRAND_ALREADY_EXIST(400, "Brand is already existed."),
    CANNOT_UPLOAD_FILE(500, "Cannot upload file."),
    NO_FILES_FOUND(400, "No files found."),
    CANNOT_GET_LINKS(500, "Can't get links."),
    CANNOT_DELETE_FILE(500, "Can't delete file."),
    CATEGORY_NOT_FOUND(400, "Category not found."),
    ACCOUNT_HAS_ONE_PROFILE(400, "Account has one profile."),;
    private final int code;
    private final String message;
}
