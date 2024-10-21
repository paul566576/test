package com.banking.gatewayserver.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;


@Configuration
public class GatewayConfig
{
	@Bean
	public RouteLocator bunkingRoutesConfig(final RouteLocatorBuilder builder)
	{
		return builder.routes()
				.route(p -> p.path("/banking/accounts/**")
						.filters(f -> f.rewritePath("/banking/accounts/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("accountsCircuitBreaker")
										.setFallbackUri("forward:/api/contactSupport"))
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
						)
						.uri("lb://ACCOUNTS"))
				.route(p -> p.path("/banking/cards/**")
						.filters(f -> f.rewritePath("/banking/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("cardsCircuitBreaker")
										.setFallbackUri("forward:/api/contactSupport"))
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)))
						.uri("lb://CARDS"))
				.route(p -> p.path("/banking/loans/**")
						.filters(f -> f.rewritePath("/banking/loans/(?<segment>.*)", "/${segment}")
										.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("loansCircuitBreaker")
										.setFallbackUri("forward:/api/contactSupport"))
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
						)
						.uri("lb://LOANS")).build();
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer()
	{
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

}
