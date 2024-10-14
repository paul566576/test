package com.banking.accounts.service.client;

import com.banking.accounts.dto.LoanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("loans")
public interface LoansFeignClient
{
	@GetMapping(value = "loans/api/fetchLoansByMobileNumber", consumes = "application/json")
	ResponseEntity<LoanDto> fetchLoanByMobileNumber(final @RequestParam String mobileNumber);
}
