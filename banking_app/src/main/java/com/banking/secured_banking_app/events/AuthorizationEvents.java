package com.banking.secured_banking_app.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class AuthorizationEvents
{
	@EventListener
	public void onFailure(final AuthorizationDeniedEvent deniedEvent)
	{
		log.warn("Authorization falid of the user: {} due to: {}", deniedEvent.getAuthentication().get().getName(),
				deniedEvent.getAuthentication().toString());
	}
}

