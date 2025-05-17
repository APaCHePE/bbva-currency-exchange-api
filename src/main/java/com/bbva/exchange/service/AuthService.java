package com.bbva.exchange.service;

import com.bbva.exchange.dto.AuthRequest;
import com.bbva.exchange.dto.AuthResponse;
import com.bbva.exchange.exception.BusinessException;
import com.bbva.exchange.model.Roles;
import com.bbva.exchange.repository.RolRepository;
import com.bbva.exchange.repository.UserRepository;
import com.bbva.exchange.repository.UserRolRepository;
import com.bbva.exchange.util.AppConstants;
import com.bbva.exchange.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserRolRepository userRolRepository;
    private final RolRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<AuthResponse> authenticate(AuthRequest request) {
        System.out.println("Authenticating user: " + request.getUsername() + ", Password: " + request.getPassword());
        return userRepository.findByUsername(request.getUsername())
                .switchIfEmpty(Mono.error(new BusinessException(AppConstants.INVALID_CREDENTIALS)))
                .doOnNext(user -> System.out.println("User found: " + user.getUsername() + ", Password: " + user.getPassword()))
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .switchIfEmpty(Mono.error(new BusinessException(AppConstants.INVALID_CREDENTIALS)))
                .flatMap(user -> userRolRepository.findByUserId(user.getId())
                        .flatMap(userRole -> roleRepository.findById(userRole.getRoleId()))
                        .map(Roles::getName)
                        .collectList()
                        .map(roles -> JwtUtil.generateToken(user.getId(), user.getUsername(), roles))
                )
                .map(token -> new AuthResponse(token))
                .onErrorMap(e -> new BusinessException(e.getMessage()));
    }
}