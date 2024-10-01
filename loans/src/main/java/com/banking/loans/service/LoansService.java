package com.banking.loans.service;

import com.banking.loans.dto.LoanDto;

import java.util.List;


public interface LoansService
{
	/**
	 * Creates new Loan based on mobile number.
	 *
	 * @param mobileNumber String
	 */
	void createLoan(final String mobileNumber);

	/**
	 * Fetch all cards by given MobileNumber.
	 *
	 * @param mobileNumber String
	 */
	List<LoanDto> fetchLoansByMobileNumber(final String mobileNumber);

	/**
	 * Fetch loan by given LoanNumber.
	 *
	 * @param loanNumber String
	 */
	LoanDto fetchLoansByLoanNumber(final String loanNumber);

	/**
	 *
	 * @param loanDto LoanDto
	 * @return boolean indicating if the update of Loan details ıs successful or not
	 */
	boolean updateLoan(final LoanDto loanDto);
}
