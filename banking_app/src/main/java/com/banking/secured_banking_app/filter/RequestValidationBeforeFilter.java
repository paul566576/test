package com.banking.secured_banking_app.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class RequestValidationBeforeFilter implements Filter
{
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException
	{
		final HttpServletRequest req = (HttpServletRequest) request;
		final HttpServletResponse res = (HttpServletResponse) response;
		String header = req.getHeader(HttpHeaders.AUTHORIZATION);
		if (null != header)
		{
			header = header.trim();
			if (StringUtils.startsWithIgnoreCase(header, "Basic "))
			{
				final byte[] baseToken = header.substring(6).getBytes(StandardCharsets.UTF_8);
				byte[] decoded;
				try
				{
					decoded = Base64.getDecoder().decode(baseToken);
					final String token = new String(decoded, StandardCharsets.UTF_8);
					int delim = token.indexOf(":");
					if (delim == -1)
					{
						throw new BadCredentialsException("Invalid basic authentication token");
					}
					final String email = token.substring(0, delim);
					if (email.toLowerCase().contains("example"))
					{
						res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					}
				}
				catch (final IllegalArgumentException e)
				{
					throw new BadCredentialsException("Failed to decode basic authentication token");
				}
			}
		}

		filterChain.doFilter(req, res);
	}
}
