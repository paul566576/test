package com.banking.cards.controller;

import com.banking.cards.constants.CardsConstants;
import com.banking.cards.dto.CardDto;
import com.banking.cards.dto.CardsContactInfoDto;
import com.banking.cards.dto.ErrorResponseDto;
import com.banking.cards.dto.ResponseDto;
import com.banking.cards.service.CardsService;
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
@RequiredArgsConstructor
@Validated
@Tag(
		name = "CRUD REST API for Cards banking",
		description = "CRUD REST API in banking to CREATE, UPDATE, FETCH and DELETE card detail"
)
@Slf4j
public class CardsController
{
	private final CardsService cardsService;
	private final Environment environment;
	private final CardsContactInfoDto cardsContactInfoDto;

	@Value("${build.version}")
	private String buildVersion;

	@Operation(
			summary = "Create card REST API",
			description = "REST API to create new card inside banking app"
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
	public ResponseEntity<ResponseDto> createCard(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 16 digits") @RequestParam String mobileNumber)
	{
		cardsService.createCard(mobileNumber);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
	}

	@Operation(
			summary = "FETCH card REST API",
			description = "REST API to fetch existing card for given card number"
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
	@GetMapping("/fetchCardByCardNumber")
	public ResponseEntity<CardDto> fetchCardByCardNumber(
			final @Pattern(regexp = "(^$|[0-9]{16})", message = "Card number must be 16 digits") @RequestParam String cardNumber)
	{
		final CardDto card = cardsService.fetchCardsByCardNumber(cardNumber);
		return ResponseEntity.status(HttpStatus.OK).body(card);
	}

	@Operation(
			summary = "FETCH card REST API",
			description = "REST API to fetch existing cards for given mobile number"
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
	@GetMapping("/fetchCardByMobileNumber")
	public ResponseEntity<CardDto> fetchCardByMobileNumber(
			final @RequestHeader("banking-correlation-id") String correlationId,
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits") @RequestParam String mobileNumber)
	{
		if (log.isDebugEnabled())
		{
			log.debug("fetchCardByMobileNumber start");
		}

		final CardDto card = cardsService.fetchCardsByMobileNumber(mobileNumber);

		if (log.isDebugEnabled())
		{
			log.debug("fetchCardByMobileNumber end");
		}

		return ResponseEntity.status(HttpStatus.OK).body(card);
	}

	@Operation(
			summary = "UPDATE card REST API",
			description = "REST API to update existing card"
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
	public ResponseEntity<ResponseDto> updateCard(final @Valid @RequestBody CardDto cardDto)
	{
		if (cardsService.updateCard(cardDto))
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDto(CardsConstants.MESSAGE_200, CardsConstants.STATUS_200));
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
	}

	@Operation(
			summary = "Delete card REST API",
			description = "REST API to delet existing card"
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
	public ResponseEntity<ResponseDto> deleteCard(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits") @RequestParam String mobileNumber)
	{
		if (cardsService.deleteCard(mobileNumber))
		{
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDto(CardsConstants.MESSAGE_200, CardsConstants.STATUS_200));
		}
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
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
		return ResponseEntity.status(HttpStatus.OK).body(CardsConstants.DEFAULT_BUILD_VERSION);
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
	@RateLimiter(name = "getJavaVersionRateLimiter", fallbackMethod = "getJavaVersionFallback")
	@Retry(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
	@GetMapping("/java-version")
	public ResponseEntity<String> getJavaVersion()
	{
		return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty(CardsConstants.JAVA_HOME));
	}

	public ResponseEntity<String> getJavaVersionFallback(final Throwable throwable)
	{
		if (log.isDebugEnabled())
		{
			log.debug("getJavaVersionFallback() method invoked");
		}
		// By default should be used java 21
		return ResponseEntity.status(HttpStatus.OK).body(CardsConstants.DEFAULT_JAVA_VERSION);
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
	public ResponseEntity<CardsContactInfoDto> getContactInfo()
	{
		if (log.isDebugEnabled())
		{
			log.debug("Invoked Accounts contact-info API");
		}
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(cardsContactInfoDto);
	}

	public ResponseEntity<CardsContactInfoDto> getContactInfoFallback(final Throwable throwable)
	{
		if (log.isDebugEnabled())
		{
			log.debug("getContactInfoFallback() method invoked");
		}

		final CardsContactInfoDto cardsContactInfoDto1 = new CardsContactInfoDto();
		cardsContactInfoDto1.setMessage(CardsConstants.WELCOME_TO_BANKING_ACCOUNTS_DEFAULT_MESSAGE);
		cardsContactInfoDto1.setOnCallSupport(CardsConstants.ON_CALL_SUPPORT_DEFAULT_PHONE_NUMBERS);
		cardsContactInfoDto1.setContactDetails(CardsConstants.DEFAULT_CONTACT_DETAILS_DATA);
		return ResponseEntity.status(HttpStatus.OK).body(cardsContactInfoDto1);
	}
}
