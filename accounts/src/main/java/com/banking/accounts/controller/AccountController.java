package com.banking.accounts.controller;

import com.banking.accounts.constants.AccountConstants;
import com.banking.accounts.dto.AccountsContactDetailsDto;
import com.banking.accounts.dto.CustomerDto;
import com.banking.accounts.dto.ErrorResponseDto;
import com.banking.accounts.dto.ResponseDto;
import com.banking.accounts.service.AccountService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(
		name = "CRUD REST API for Accounts banking",
		description = "CRUD REST API in banking  to CREATE, UPDATE, FETCH and DELETE account detail"
)
public class AccountController
{
	private final AccountService accountService;
	private final Environment environment;
	private final AccountsContactDetailsDto accountsContactDetailsDto;

	@Value("${build.version}")
	private String buildVersion;

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
				.body(new ResponseDto(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_UPDATE));
	}

	@Operation(
			summary = "FETCH account REST API",
			description = "REST API to delete existing customer and account"
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

	@Operation(
			summary = "Get Build information",
			description = "Get Build information that is deployed into accounts microservice"
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
	@RateLimiter(name = "getBuildInfoRateLimiter", fallbackMethod = "getBuildInfoFallback")
	@Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInformation()
	{
		if (log.isDebugEnabled())
		{
			log.debug("getBuildInfo() method invoked");
		}
		//		Just example for checking ignore exception functionality
		//		throw new NullPointerException();
		return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
	}

	public ResponseEntity<String> getBuildInfoFallback(final Throwable throwable)
	{
		if (log.isDebugEnabled())
		{
			log.debug("getBuildInfoFallback() method invoked");
		}
		return ResponseEntity.status(HttpStatus.OK).body(AccountConstants.DEFAULT_BUILD_VERSION);
	}

	@Operation(
			summary = "Get Java Version",
			description = "Get Java versions details that is installed into accounts microservice"
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
	@RateLimiter(name = "getJavaVersionRateLimiter", fallbackMethod = "getJavaVersionFallback")
	@Retry(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
	@GetMapping("/java-version")
	public ResponseEntity<String> getJavaVersion()
	{
		return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty(AccountConstants.JAVA_HOME));
	}

	public ResponseEntity<String> getJavaVersionFallback(final Throwable throwable)
	{
		if (log.isDebugEnabled())
		{
			log.debug("getJavaVersionFallback() method invoked");
		}
		// By default should be used java 21
		return ResponseEntity.status(HttpStatus.OK).body(AccountConstants.DEFAULT_JAVA_VERSION);
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
	@RateLimiter(name = "getContactInfoRateLimiter", fallbackMethod = "getContactInfoFallback")
	@Retry(name = "getContactInfo", fallbackMethod = "getContactInfoFallback")
	@GetMapping("/contact-info")
	public ResponseEntity<AccountsContactDetailsDto> getContactInfo()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Invoked Accounts contact-info API");
		}
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(accountsContactDetailsDto);
	}

	public ResponseEntity<AccountsContactDetailsDto> getContactInfoFallback(final Throwable throwable	)
	{
		if (log.isDebugEnabled())
		{
			log.debug("getContactInfoFallback() method invoked");
		}

		return ResponseEntity.status(HttpStatus.OK).body(getContactInfoFallBack());
	}

	private AccountsContactDetailsDto getContactInfoFallBack() {
		final AccountsContactDetailsDto asd = new AccountsContactDetailsDto();
		asd.setMessage(AccountConstants.WELCOME_TO_BANKING_ACCOUNTS_DEFAULT_MESSAGE);
		asd.setOnCallSupport(AccountConstants.ON_CALL_SUPPORT_DEFAULT_PHONE_NUMBERS);
		asd.setContactDetails(AccountConstants.DEFAULT_CONTACT_DETAILS_DATA);
		return asd;
	}
}
