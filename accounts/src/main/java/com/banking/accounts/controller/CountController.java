package com.banking.accounts.controller;

import com.banking.accounts.aspect.RestCounterAspect;
import com.banking.accounts.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;


@RestController
@RequestMapping("/accounts/counter")
@Tag(
		name = "Temporary REST API for Accounts banking MS to get count of call of all end points",
		description = "Temporary REST API in banking account MS to calculate sum of all endpoints calll"
)
public class CountController
{

	@Operation(
			summary = "FETCH account REST API call count",
			description = "REST API to fetch calculation result of calling each endpoint"
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
	@GetMapping("/getCount")
	public ResponseEntity<AtomicLong> getCount() {
		return ResponseEntity.ok(RestCounterAspect.COUNTER);
	}
}
