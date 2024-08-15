package com.banking.secured_banking_app.exceptionhandlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;


public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint

{
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException
	{
		final LocalDateTime timeStamp = LocalDateTime.now();
		final String message = (authException != null && authException.getMessage() != null) ?
				authException.getMessage() :
				"Authentication failed";
		final String path = request.getRequestURI();

		response.setHeader("bank-app-error-reason", "Authentication failed");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		final String jsonResponse = String.format(
				"{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
				timeStamp, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
				message, path);
		response.getWriter().write(jsonResponse);


	}
}
