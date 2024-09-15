package com.banking.secured_banking_app.filter;

import com.banking.secured_banking_app.constats.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class JwtTokenValidatorFilter extends OncePerRequestFilter
{
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain)
			throws ServletException, IOException
	{
		final String jwt = request.getHeader(ApplicationConstants.JWT_HEADER);

		if (StringUtils.isNotBlank(jwt))
		{
			try
			{
				final String secret = getEnvironment().getProperty(ApplicationConstants.JWT_SECRET_KEY,
						ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
				final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
				if (null != secretKey)
				{
					final Claims claims = Jwts.parser().verifyWith(secretKey).build()
							.parseClaimsJws(jwt).getPayload();
					final String userName = String.valueOf(claims.get("username"));
					final String authorities = String.valueOf(claims.get("authorities"));
					final Authentication authentication = new UsernamePasswordAuthenticationToken(userName, null,
							AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
			catch (final Exception e)
			{
				throw new BadCredentialsException("Invalid JWT token", e);
			}
		}

		filterChain.doFilter(request, response);
	}

	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
	{
		return request.getServletPath().equals("/user");
	}
}
