package com.banking.accounts.service.client;

import com.banking.accounts.dto.LoanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "loans", fallback = LoansFallback.class)
public interface LoansFeignClient
{
	@GetMapping(value = "/api/fetchLoansByMobileNumber", consumes = "application/json")
	ResponseEntity<LoanDto> fetchLoanByMobileNumber(final @RequestHeader("banking-correlation-id") String correlationId,
			@RequestParam String mobileNumber);
}
