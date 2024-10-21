package com.banking.loans.controller;

import com.banking.loans.constants.LoansConstants;
import com.banking.loans.dto.ErrorResponseDto;
import com.banking.loans.dto.LoanDto;
import com.banking.loans.dto.LoansContactInfoDto;
import com.banking.loans.dto.ResponseDto;
import com.banking.loans.service.LoansService;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
@Tag(
		name = "CRUD REST API for Loans banking",
		description = "CRUD REST API in banking to CREATE, UPDATE, FETCH and DELETE loan detail"
)
@Slf4j
public class LoansController
{
	private final LoansService loansService;
	private final LoansContactInfoDto loansContactInfoDto;
	private final Environment environment;

	@Value("${build.version}")
	private String buildVersion;

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
			final @RequestHeader("banking-correlation-id") String correlationId,
			final @Pattern(regexp = "(^$|[0-9]{12})", message = "Loan number must be 12 digits")
			@RequestParam String loanNumber)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Banking correlation id found: {}", correlationId);
		}

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
	public ResponseEntity<LoanDto> fetchLoanByMobileNumber(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits")
			@RequestParam String mobileNumber)
	{
		final LoanDto loan = loansService.fetchLoansByMobileNumber(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(loan);
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
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 12 digits")
			@RequestParam String mobileNumber)
	{
		if (loansService.deleteLoan(mobileNumber))
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDto(LoansConstants.MESSAGE_200, LoansConstants.STATUS_200));
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
	}

	@Operation(
			summary = "Get Build information",
			description = "Get Build information that is deployed into cards microservice"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDto.class)
					)
			)
	}
	)
	@Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInformation()
	{
		if (log.isDebugEnabled())
		{
			log.debug("getBuildInfo() method invoked");
		}
//				Just example for checking ignore exception functionality
//				throw new NullPointerException();
		return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
	}

	public ResponseEntity<String> getBuildInfoFallback(final Throwable throwable)
	{
		if (log.isDebugEnabled())
		{
			log.debug("getBuildInfoFallback() method invoked");
		}
		return ResponseEntity.status(HttpStatus.OK).body(LoansConstants.DEFAULT_BUILD_VERSION);
	}

	@Operation(
			summary = "Get Java version",
			description = "Get Java versions details that is installed into cards microservice"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDto.class)
					)
			)
	}
	)
	@Retry(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
	@GetMapping("/java-version")
	public ResponseEntity<String> getJavaVersion()
	{
		return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty(LoansConstants.JAVA_HOME));
	}

	public ResponseEntity<String> getJavaVersionFallback()
	{
		if (log.isDebugEnabled())
		{
			log.debug("getJavaVersionFallback() method invoked");
		}
		// By default should be used java 21
		return ResponseEntity.status(HttpStatus.OK).body(LoansConstants.DEFAULT_JAVA_VERSION);
	}

	@Operation(
			summary = "Get Contact Info",
			description = "Contact Info details that can be reached out in case of any issues"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDto.class)
					)
			)
	}
	)
	@Retry(name = "getContactInfo", fallbackMethod = "getContactInfoFallback")
	@GetMapping("/contact-info")
	public ResponseEntity<LoansContactInfoDto> getContactInfo()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Invoked Accounts contact-info API");
		}
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(loansContactInfoDto);
	}

	public ResponseEntity<LoansContactInfoDto> getContactInfoFallback()
	{
		if (log.isDebugEnabled())
		{
			log.debug("getContactInfoFallback() method invoked");
		}

		final LoansContactInfoDto asd = new LoansContactInfoDto();
		asd.setMessage(LoansConstants.WELCOME_TO_BANKING_ACCOUNTS_DEFAULT_MESSAGE);
		asd.setOnCallSupport(LoansConstants.ON_CALL_SUPPORT_DEFAULT_PHONE_NUMBERS);
		asd.setContactDetails(LoansConstants.DEFAULT_CONTACT_DETAILS_DATA);
		return ResponseEntity.status(HttpStatus.OK).body(asd);
	}
}
