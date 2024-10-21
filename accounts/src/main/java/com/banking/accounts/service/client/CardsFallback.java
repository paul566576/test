package com.banking.accounts.service.client;

import com.banking.accounts.dto.CardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class CardsFallback implements CardsFeignClient
{
	@Override
	public ResponseEntity<CardDto> fetchCardByMobileNumber(final String correlationId, final String mobileNumber)
	{
		log.warn("Connection to Cards MS has is failed.");
		// We have to return null instead of RuntimeException, because cards data is only part of complex CustomerDetailsDto
		return null;
	}
}
