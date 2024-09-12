package com.banking.secured_banking_app.config;

import com.banking.secured_banking_app.exceptionhandlers.CustomAccessDeniedHandler;
import com.banking.secured_banking_app.exceptionhandlers.CustomBasicAuthenticationEntryPoint;
import com.banking.secured_banking_app.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@Configuration
@Profile("prod")
public class AppSecurityProdConfig
{
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity http) throws Exception
	{
		final CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
		http.cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource()
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
				.securityContext(context -> context.requireExplicitSave(false))
				.sessionManagement(sessionCofig -> sessionCofig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
				.csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
						.ignoringRequestMatchers("/contacts", "/register", "/apiLogin")
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
				.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
				.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
				.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
				.addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/myAccount").hasRole("USER")
						.requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/myCards").hasRole("USER")
						.requestMatchers("/myLoans").hasRole("USER")
						.requestMatchers("/user").authenticated()
						.requestMatchers("/notices", "/contacts", "/register", "/error", "/invalidSession", "/apiLogin").permitAll());
		http.formLogin(Customizer.withDefaults());
		http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
		//		http.exceptionHandling(ehc -> ehc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
		http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker()
	{
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}
}
