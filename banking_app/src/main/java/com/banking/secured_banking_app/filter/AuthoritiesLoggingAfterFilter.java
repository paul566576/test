package com.banking.secured_banking_app.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;


@Slf4j
public class AuthoritiesLoggingAfterFilter implements Filter
{
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication)
 		{
			log.info("Authorities logged in : {}",
					authentication.getName() + "is si=uccessfully authenticated and has authorities " + authentication.getAuthorities()
							.toString());
		}
		chain.doFilter(request, response);
	}
}
