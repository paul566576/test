package com.banking.secured_banking_app.exceptionhandlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;


public class CustomAccessDeniedHandler implements AccessDeniedHandler
{
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException, ServletException
	{
		final LocalDateTime timeStamp = LocalDateTime.now();
		final String message = (accessDeniedException != null && accessDeniedException.getMessage() != null) ?
				accessDeniedException.getMessage() :
				"Authentication failed";
		final String path = request.getRequestURI();

		response.setHeader("bank-app-denied-reason", "Authentication failed");
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("application/json");
		final String jsonResponse = String.format(
				"{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
				timeStamp, HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
				message, path);
		response.getWriter().write(jsonResponse);
	}
}
