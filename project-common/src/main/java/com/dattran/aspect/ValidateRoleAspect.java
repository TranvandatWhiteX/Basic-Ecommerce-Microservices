package com.dattran.aspect;

import com.dattran.annotations.HasRoles;
import com.dattran.enums.ResponseStatus;
import com.dattran.exceptions.AppException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ValidateRoleAspect {
    HttpServletRequest httpServletRequest;

    @Before("@annotation(hasRoles)")
    public void validateRoles(HasRoles hasRoles) {
        String roles = httpServletRequest.getHeader("X-Roles");
        log.info("Validating roles: {}", roles);
        for (String role : hasRoles.roles()) {
            if (roles.contains(role)) {
                return;
            }
        }
        throw new AppException(ResponseStatus.FORBIDDEN);
    }
}
