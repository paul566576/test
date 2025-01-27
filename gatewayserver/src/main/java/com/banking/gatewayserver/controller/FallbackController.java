package com.banking.gatewayserver.controller;

import com.banking.gatewayserver.constatnts.GatewayConstants;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
public class FallbackController
{
	@RequestMapping("/contactSupport")	public Mono<String> contactSupport()
	{
		return Mono.just(GatewayConstants.CONTACT_SUPPORT_ERROR_MESSAGE);
	}
}
