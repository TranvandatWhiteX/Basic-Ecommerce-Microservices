package com.dattran.annotations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RolesValidator implements ConstraintValidator<HasRoles, Object> {
    HttpServletRequest httpServletRequest;
    @NonFinal
    String[] roles;

    @Override
    public void initialize(HasRoles constraintAnnotation) {
        this.roles = constraintAnnotation.roles();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        String rolesHeader = httpServletRequest.getHeader("X-Roles");
        if (rolesHeader != null) {
            for (String role : roles) {
                if (rolesHeader.contains(role)) {
                    return true;
                }
            }
        }
        log.error("Required roles not found, throwing exception");
        return false;
    }
}
