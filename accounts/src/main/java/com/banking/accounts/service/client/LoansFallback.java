package com.banking.accounts.service.client;

import com.banking.accounts.dto.LoanDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class LoansFallback implements LoansFeignClient
{
	@Override
	public ResponseEntity<LoanDto> fetchLoanByMobileNumber(final String correlationId, final String mobileNumber)
	{
		log.warn("Connection to Loans MS has is failed.");
		// We have to return null instead of RuntimeException, because loans data is only part of complex CustomerDetailsDto
		return null;
	}
}
