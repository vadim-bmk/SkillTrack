package com.dvo.user_service.aop;

import com.dvo.user_service.security.AppUserPrincipal;
import com.dvo.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AccessAspect {
    private final UserService userService;

    @Before("@annotation(CheckAccessToUser)")
    public void checkAccessToUser(JoinPoint joinPoint) {
        Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principalObj instanceof AppUserPrincipal)) {
            throw new AccessDeniedException("Unauthorized access");
        }

        AppUserPrincipal principal = (AppUserPrincipal) principalObj;
        Long currentUserId = principal.getId();
        String role = principal.getAuthorities().iterator().next().getAuthority();

        Object[] args = joinPoint.getArgs();
        Long requestedUserId = null;
        for (Object arg : args) {
            if (arg instanceof Long) {
                requestedUserId = (Long) arg;
                break;
            }
        }

        if (requestedUserId == null) {
            throw new IllegalArgumentException("Invalid argument type: expected userId as Long");
        }

        if ("ROLE_ADMIN".equals(role) || "ROLE_MANAGER".equals(role)) {
            return;
        }

        if ("ROLE_EMPLOYEE".equals(role) && !requestedUserId.equals(currentUserId)) {
            throw new AccessDeniedException("EMPLOYEE can only access their own data");
        }

    }
}
