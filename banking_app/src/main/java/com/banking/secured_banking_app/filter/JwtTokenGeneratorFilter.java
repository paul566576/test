package com.banking.secured_banking_app.filter;

import com.banking.secured_banking_app.controller.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;


public class JwtTokenGeneratorFilter extends OncePerRequestFilter
{
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain)
			throws ServletException, IOException
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null)
		{
			final String secret = getEnvironment().getProperty(ApplicationConstants.JWT_SECRET_KEY,
					ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
			final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
			final String jwt = Jwts.builder().issuer("Banking app").subject("JWT token")
					.claim("username", authentication.getName())
					.claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
							.collect(Collectors.joining("."))).issuedAt(new Date())
					.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)).signWith(secretKey).compact();
			response.setHeader(ApplicationConstants.JWT_HEADER, jwt);
		}
		filterChain.doFilter(request, response);
	}

	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
	{
		return !request.getServletPath().equals("/user");
	}
}
