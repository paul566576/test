package com.banking.secured_banking_app.config;

import com.banking.secured_banking_app.exceptionhandlers.CustomAccessDeniedHandler;
import com.banking.secured_banking_app.exceptionhandlers.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;


@Configuration
@Profile("prod")
public class AppSecurityProdConfig
{
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception
	{
		http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession").maximumSessions(3).maxSessionsPreventsLogin(true))
				.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
				.csrf(csrfConfig -> csrfConfig.disable())
				.authorizeHttpRequests((requests) -> {
					requests.requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans", "/user").authenticated()
							.requestMatchers("/notices", "/contacts", "/register", "/error", "/invalidSession").permitAll();
				});
		http.formLogin(Customizer.withDefaults() );
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
