package com.authserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class BankingUserNamePwdAuthenticationProvider implements AuthenticationProvider
{

	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException
	{
		final String userName = authentication.getName();
		final String password = authentication.getCredentials().toString();
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		if (!passwordEncoder.matches(password, userDetails.getPassword()))
		{
			throw new BadCredentialsException("Bad credentials");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(final Class<?> authentication)
	{
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
