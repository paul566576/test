package com.banking.secured_banking_app.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class AuthenticationEvents
{

	@EventListener
	public void onSuccess(final AuthenticationSuccessEvent successEvent)
	{
		log.info("Authentication successful for user: {}", successEvent.getAuthentication().getName());
	}

	@EventListener
	public void oFailure(final AbstractAuthenticationFailureEvent failureEvent)
	{
		log.info("Authentication failure for user: {} due to: {}", failureEvent.getAuthentication().getName(),
				failureEvent.getException().getMessage());
	}
}
