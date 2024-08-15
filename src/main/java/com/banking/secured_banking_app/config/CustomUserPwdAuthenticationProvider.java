package com.banking.secured_banking_app.config;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class CustomUserPwdAuthenticationProvider implements AuthenticationProvider
{
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		final String username = authentication.getName();
		final String password = authentication.getCredentials().toString();
		final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		if (passwordEncoder.matches(password, userDetails.getPassword()))
		{
			return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
		}
		else
		{
			throw new BadCredentialsException("Bad credentials");
		}
	}

	@Override
	public boolean supports(Class<?> authentication)
	{
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));

	}
}
