package com.banking.accounts.service.client;

import com.banking.accounts.dto.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "cards", url = "http://cards:9000", fallback = CardsFallback.class)
public interface CardsFeignClient
{
	@GetMapping(value = "/api/fetchCardByMobileNumber", consumes = "application/json")
	ResponseEntity<CardDto> fetchCardByMobileNumber(final @RequestHeader("banking-correlation-id") String correlationId,
			final @RequestParam String mobileNumber);
}
