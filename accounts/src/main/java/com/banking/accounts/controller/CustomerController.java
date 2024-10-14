package com.banking.accounts.controller;

import com.banking.accounts.dto.CustomerDetailsDto;
import com.banking.accounts.dto.CustomerDto;
import com.banking.accounts.dto.ErrorResponseDto;
import com.banking.accounts.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/accounts/api", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
@RequiredArgsConstructor
@Tag(
		name = "CRUD REST API for Customer in banking",
		description = "CRUD REST API in banking to FETCH customer detail"
)
public class CustomerController
{
	private final CustomerService customerService;

	@Operation(
			summary = "FETCH CustomerDetails REST API",
			description = "REST API to fetch existing customer, card, loans and account info for given mobile number"
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
	@GetMapping("/fetchCustomerDetails")
	public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits")
			@RequestParam String mobileNumber)
	{
		final CustomerDetailsDto customerDetailsDto = customerService.fetchCustomerDetailsByMobileNumber(mobileNumber);

		return ResponseEntity.status(HttpStatus.OK).body(customerDetailsDto);

	}
}
