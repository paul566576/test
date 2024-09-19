package com.banking.secured_banking_app.config;

import com.banking.secured_banking_app.exceptionhandlers.CustomAccessDeniedHandler;
import com.banking.secured_banking_app.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@Configuration
@Profile("!prod")
public class AppSecurityConfig
{
//	@Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
//	private String introspectionUri;
//	@Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
//	private String clientId;
//	@Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
//	private String clientSecret;

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception
	{
		final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
		final CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
		http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource()
				{
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request)
					{
						final CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.setAllowCredentials(true);
						config.setAllowedHeaders(Collections.singletonList("*"));
						config.setExposedHeaders(Collections.singletonList("Authorization"));
						config.setMaxAge(3600L);
						return config;
					}
				}))

				.csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
						.ignoringRequestMatchers("/contacts", "/register")
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
				.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
				.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/myAccount").hasRole("USER")
						.requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/myCards").authenticated()
						.requestMatchers("/myLoans").hasRole("USER")
						.requestMatchers("/user").authenticated()
						.requestMatchers("/notices", "/contacts", "/register", "/error").permitAll());
				http.oauth2ResourceServer(
						rsc -> rsc.jwt(jwtConfigurre -> jwtConfigurre.jwtAuthenticationConverter(jwtAuthenticationConverter)));
//		http.oauth2ResourceServer(rsc -> rsc.opaqueToken(
//				otc -> otc.authenticationConverter(new KeycloakOpaqueRoleConverter()).introspectionUri(introspectionUri)
//						.introspectionClientCredentials(clientId, clientSecret)));
		http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
		return http.build();
	}
}
