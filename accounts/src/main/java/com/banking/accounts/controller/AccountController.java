package com.banking.accounts.controller;

import com.banking.accounts.constants.AccountConstants;
import com.banking.accounts.dto.CustomerDto;
import com.banking.accounts.dto.ErrorResponseDto;
import com.banking.accounts.dto.ResponseDto;
import com.banking.accounts.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "/accounts/api", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
@RequiredArgsConstructor
@Tag(
		name = "CRUD REST API for Accounts banking",
		description = "CRUD REST API in banking  to CREATE, UPDATE, FETCH and DELETE account detail"
)
public class AccountController
{
	private final AccountService accountService;

	@Operation(
			summary = "Create account REST API",
			description = "REST API to create new Customer and Account inside banking app"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "HTTP status CREATED"
			),
			@ApiResponse(
					responseCode = "500",
					description = "Interval Server Error",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createAccount(final @Valid @RequestBody CustomerDto customer)
	{
		accountService.createAccount(customer);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(AccountConstants.STATUS_201, AccountConstants.MESSAGE_201));
	}

	@Operation(
			summary = "FETCH account REST API",
			description = "REST API to fetch existing customer and account  for given mobile number"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "Interval Server Error",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	@GetMapping("/fetch")
	public ResponseEntity<CustomerDto> fetchAccountDetails(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits")
			@RequestParam String mobileNumber)
	{
		final CustomerDto customerDto = accountService.getCustomerByMobileNumber(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(customerDto);
	}

	@Operation(
			summary = "UPDATE account REST API",
			description = "REST API to update existing customer and account"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP status UPDATED"
			),
			@ApiResponse(
					responseCode = "417",
					description = "HTTP status Update Server Error"
			),
			@ApiResponse(
					responseCode = "500",
					description = "Interval Server Error",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	@PutMapping("/update")
	public ResponseEntity<ResponseDto> updateAccount(final @Valid @RequestBody CustomerDto customerDto)
	{
		if (accountService.updateAccount(customerDto))
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDto(AccountConstants.MESSAGE_200, AccountConstants.STATUS_200));
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(AccountConstants.MESSAGE_417_UPDATE, AccountConstants.MESSAGE_417_UPDATE));
	}

	@Operation(
			summary = "FETCH account REST API",
			description = "REST API to update existing customer and account"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP status DELETED"
			),
			@ApiResponse(
					responseCode = "417",
					description = "HTTP status Update Server Error"
			),
			@ApiResponse(
					responseCode = "500",
					description = "Interval Server Error",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
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
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_DELETE));
	}
}
