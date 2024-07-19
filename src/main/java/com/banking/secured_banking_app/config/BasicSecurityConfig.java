package com.banking.secured_banking_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class BasicSecurityConfig
{

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception
	{
		http.authorizeHttpRequests((requests) -> {
			requests.requestMatchers("/myAccount", "/myBalance", "/myCards", "myLoans").authenticated()
					.requestMatchers("/notices", "/contacts", "/error").permitAll();
		});
		http.formLogin(Customizer.withDefaults());
		http.httpBasic(Customizer.withDefaults());
		return http.build();
	}
	}
