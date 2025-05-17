package com.bbva.exchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	private final JwtAuthenticationManager jwtAuthenticationManager;

	public SecurityConfig(JwtAuthenticationManager jwtAuthenticationManager) {
		this.jwtAuthenticationManager = jwtAuthenticationManager;
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http
			.csrf(ServerHttpSecurity.CsrfSpec::disable)
			.securityContextRepository(jwtAuthenticationManager)
			.authorizeExchange(exchanges -> exchanges
				.pathMatchers(
					"/healths",
					"/auth/login",
					"/swagger-ui.html",
					"/swagger-ui/**",
					"/swagger-ui/index.html",
					"/v3/api-docs",
					"/v3/api-docs/**",
					"/v3/api-docs.yaml",
					"/api-docs/**",
					"/webjars/**",
					"/swagger-resources/**",
					"/swagger-resources"
				).permitAll()
				.pathMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
				.anyExchange().authenticated()
			)
			.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
			.build();
	}
}