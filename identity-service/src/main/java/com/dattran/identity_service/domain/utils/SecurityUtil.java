package com.dattran.identity_service.domain.utils;

import com.dattran.identity_service.domain.entities.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    public Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof Account) {
                return (Account) authentication.getPrincipal();
            }
            return null;
        }
        return null;
    }
}
