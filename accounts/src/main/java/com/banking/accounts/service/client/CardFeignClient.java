package com.banking.accounts.service.client;

import com.banking.accounts.dto.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("cards")
public interface CardFeignClient
{
	@GetMapping(value = "/api/fetchCardByMobileNumber", consumes = "application/json")
	ResponseEntity<CardDto> fetchCardByMobileNumber(final @RequestParam String mobileNumber);
}
