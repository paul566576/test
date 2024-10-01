package com.banking.loans.controller;

import com.banking.loans.constants.LoansConstants;
import com.banking.loans.dto.ErrorResponseDto;
import com.banking.loans.dto.LoanDto;
import com.banking.loans.dto.ResponseDto;
import com.banking.loans.service.LoansService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/loans/api")
@Validated
@RequiredArgsConstructor
@Tag(
		name = "CRUD REST API for Loans banking",
		description = "CRUD REST API in banking to CREATE, UPDATE, FETCH and DELETE loan detail"
)
public class LoansController
{
	private final LoansService loansService;

	@Operation(
			summary = "CREATE loan REST API",
			description = "REST API to create new loan inside banking app"
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
	public ResponseEntity<ResponseDto> createLoan(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number length must be 11 digits")
			@RequestParam String mobileNumber)
	{
		loansService.createLoan(mobileNumber);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
	}

	@Operation(
			summary = "FETCH loan REST API",
			description = "REST API to fetch existing loan for given loan number"
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
	@GetMapping("/fetchLoanByNumber")
	public ResponseEntity<LoanDto> fetchLoanByLoanNumber(
			final @Pattern(regexp = "(^$|[0-9]{12})", message = "Loan number must be 12 digits")
			@RequestParam String loanNumber)
	{
		final LoanDto loan = loansService.fetchLoansByLoanNumber(loanNumber);
		return ResponseEntity.status(HttpStatus.OK).body(loan);
	}

	@Operation(
			summary = "FETCH loan REST API",
			description = "REST API to fetch existing loan for given mobile number"
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
	@GetMapping("/fetchLoansByMobileNumber")
	public ResponseEntity<List<LoanDto>> fetchLoanByMobileNumber(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits")
			@RequestParam String mobileNumber)
	{
		final List<LoanDto> loans = loansService.fetchLoansByMobileNumber(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(loans);
	}

	@Operation(
			summary = "UPDATE loan REST API",
			description = "REST API to update existing loan"
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
	public ResponseEntity<ResponseDto> updateLoan(final @Valid @RequestBody LoanDto loanDto)
	{
		if (loansService.updateLoan(loanDto))
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDto(LoansConstants.MESSAGE_200, LoansConstants.STATUS_200));
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
	}

	@Operation(
			summary = "DELETE loan REST API",
			description = "REST API to delete existing card"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP status DELETED"
			),
			@ApiResponse(
					responseCode = "417",
					description = "HTTP status Delete Server Error"
			),
			@ApiResponse(
					responseCode = "500",
					description = "Interval Server Error",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseDto> deleteLoan(
			final @Pattern(regexp = "(^$|[0-9]{12})", message = "Loan number must be 12 digits")
			@RequestParam String loanNumber)
	{
		if (loansService.deleteLoan(loanNumber))
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDto(LoansConstants.MESSAGE_200, LoansConstants.STATUS_200));
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
	}
}
