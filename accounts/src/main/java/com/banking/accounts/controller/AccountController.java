package com.banking.accounts.controller;

import com.banking.accounts.constants.AccountConstants;
import com.banking.accounts.dto.CustomerDto;
import com.banking.accounts.dto.ResponseDto;
import com.banking.accounts.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor
public class AccountController
{
	private final AccountService accountService;

	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createAccount(final @RequestBody CustomerDto customer)
	{
		accountService.createAccount(customer);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(AccountConstants.STATUS_201, AccountConstants.MESSAGE_201));
	}

	@GetMapping("/fetch")
	public ResponseEntity<CustomerDto> fetchAccountDetails(final @RequestParam String mobileNumber)
	{
		final CustomerDto customerDto = accountService.getCustomerByMobileNumber(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(customerDto);
	}
}
