package com.banking.gatewayserver.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;


@Component
public class FilterUtility
{
	public static final String CORRELATION_ID = "banking-correlation-id";

	public String getCorrelationId(final HttpHeaders requestHeaders)
	{
		if (requestHeaders.get(CORRELATION_ID) != null) {
			final List<String> requestHeadersList = requestHeaders.get(CORRELATION_ID);
			return requestHeadersList.stream().findFirst().get();
		}
		return null;
	}

	public ServerWebExchange setRequestHeader(final ServerWebExchange exchange, final String name, final String value) {
		return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
	}

	public ServerWebExchange setCorrelationId(final ServerWebExchange exchange, final String correlationId) {
		return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
	}
}
