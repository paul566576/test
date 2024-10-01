package com.banking.loans.controller;

import com.banking.loans.constants.LoansConstants;
import com.banking.loans.dto.LoanDto;
import com.banking.loans.dto.ResponseDto;
import com.banking.loans.service.LoansService;
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
public class LoansController
{
	private final LoansService loansService;

	@RequestMapping("/create")
	public ResponseEntity<ResponseDto> createLoan(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number length must be 11 digits")
			@RequestParam String mobileNumber)
	{
		loansService.createLoan(mobileNumber);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
	}

	@GetMapping("/fetchLoanByNumber")
	public ResponseEntity<LoanDto> fetchLoanByLoanNumber(
			final @Pattern(regexp = "(^$|[0-9]{12})", message = "Loan number must be 12 digits")
			@RequestParam String loanNumber)
	{
		final LoanDto loan = loansService.fetchLoansByLoanNumber(loanNumber);
		return ResponseEntity.status(HttpStatus.OK).body(loan);
	}

	@GetMapping("/fetchLoansByMobileNumber")
	public ResponseEntity<List<LoanDto>> fetchLoanByMobileNumber(
			final @Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits")
			@RequestParam String mobileNumber)
	{
		final List<LoanDto> loans = loansService.fetchLoansByMobileNumber(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(loans);
	}

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

}
