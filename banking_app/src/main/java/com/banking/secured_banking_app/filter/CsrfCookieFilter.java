package com.banking.secured_banking_app.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class CsrfCookieFilter extends OncePerRequestFilter
{
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain)
			throws ServletException, IOException
	{
		final CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		// Render the token value to a cookie by causing the deferred token to be loaded
		csrfToken.getToken();
		filterChain.doFilter(request, response);
	}
}
