package com.dattran.customer_service.domain.aspect;

import com.dattran.customer_service.domain.annotations.HasRoles;
import com.dattran.enums.ResponseStatus;
import com.dattran.exceptions.AppException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ValidateRoleAspect {
    HttpServletRequest httpServletRequest;

    @Before("@annotation(hasRoles)")
    public void validateRoles(HasRoles hasRoles) {
        String roles = httpServletRequest.getHeader("X-Roles");
        for (String role : hasRoles.roles()) {
            if (roles.contains(role)) {
                return;
            }
        }
        throw new AppException(ResponseStatus.UNAUTHORIZED);
    }
}
