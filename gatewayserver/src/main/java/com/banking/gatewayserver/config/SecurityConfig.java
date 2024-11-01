package com.banking.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig
{

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http)
	{
		http.authorizeExchange(exchanges -> exchanges
						.pathMatchers("/banking/accounts/**").hasRole("ACCOUNTS")
						.pathMatchers("/banking/cards/**").hasRole("CARDS")
						.pathMatchers("/banking/loans/**").hasRole("LOANS"))
				.oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
						.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(grantedAuthorityExtractor())));

		// Disable because I am not going to use browser for that pet project
		http.csrf(csrfSpec -> csrfSpec.disable());
		return http.build();
	}

	private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthorityExtractor()
	{
		final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
		return new ReactiveJwtAuthenticationConverterAdapter(converter);
	}
}
