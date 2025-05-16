package com.bbva.exchange.util;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

import java.util.List;

public class SecurityUtil {

    public static Mono<Void> requireAdmin() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(context -> {
                    String authorities = context.getAuthentication().getAuthorities().toString();
                    System.out.println("Authorities: " + authorities);
                    if (authorities.contains(AppConstants.ROLE_ADMIN)) {
                        return Mono.empty();
                    } else {
                        return Mono.error(new com.bbva.exchange.exception.BusinessException(AppConstants.ACCESS_DENIED_ADMIN));
                    }
                });
    }
}