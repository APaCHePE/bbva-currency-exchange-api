package com.bbva.exchange.config;

import com.bbva.exchange.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationManager implements ServerSecurityContextRepository {

    @Override
    public Mono<Void> save(ServerWebExchange exchange, org.springframework.security.core.context.SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<org.springframework.security.core.context.SecurityContext> load(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Token: " + token);
            try {
                Claims claims = JwtUtil.validateToken(token);
                String username = claims.getSubject();
                List<String> roles = (List<String>) claims.get("roles");
                System.out.println("Username: " + username);
                System.out.println("Roles: " + roles);
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                return Mono.just(new SecurityContextImpl(authentication));
            } catch (Exception e) {
                return Mono.empty();
            }
        }

        return Mono.empty();
    }
}
