package com.banking.secured_banking_app.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class AuthoritiesLoggingAtFilter implements Filter
{
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		log.info("AuthoritiesLoggingAtFilter validation is processing...");
		chain.doFilter(request, response);
	}
}
