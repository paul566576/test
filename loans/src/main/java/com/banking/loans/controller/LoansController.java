package com.banking.loans.controller;

import com.banking.loans.constants.LoansConstants;
import com.banking.loans.dto.ResponseDto;
import com.banking.loans.service.LoansService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/loans/api")
@Validated
@RequiredArgsConstructor
public class LoansController
{
	private final LoansService loansService;

	@RequestMapping("/create")
	public ResponseEntity<ResponseDto> createLoan(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number length must be 11 digits")
			@RequestBody String mobileNumber)
	{
		loansService.createLoan(mobileNumber);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
	}

}
