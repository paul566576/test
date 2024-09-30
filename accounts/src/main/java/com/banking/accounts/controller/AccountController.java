package com.banking.accounts.controller;

import com.banking.accounts.constants.AccountConstants;
import com.banking.accounts.dto.CustomerDto;
import com.banking.accounts.dto.ResponseDto;
import com.banking.accounts.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
@RequiredArgsConstructor
public class AccountController
{
	private final AccountService accountService;

	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createAccount(final @Valid @RequestBody CustomerDto customer)
	{
		accountService.createAccount(customer);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(AccountConstants.STATUS_201, AccountConstants.MESSAGE_201));
	}

	@GetMapping("/fetch")
	public ResponseEntity<CustomerDto> fetchAccountDetails(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits")
			@RequestParam String mobileNumber)
	{
		final CustomerDto customerDto = accountService.getCustomerByMobileNumber(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(customerDto);
	}

	@PutMapping("/update")
	public ResponseEntity<ResponseDto> updateAccount(final @Valid @RequestBody CustomerDto customerDto)
	{
		if (accountService.updateAccount(customerDto))
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDto(AccountConstants.MESSAGE_200, AccountConstants.STATUS_200));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseDto(AccountConstants.MESSAGE_417_UPDATE, AccountConstants.MESSAGE_417_UPDATE));
	}

	@DeleteMapping("/delete")

	public ResponseEntity<ResponseDto> deleteAccount(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits")
			@RequestParam String mobileNumber)
	{
		if (accountService.deleteAccount(mobileNumber))
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDto(AccountConstants.MESSAGE_200, AccountConstants.STATUS_200));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseDto(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_DELETE));
	}
}
