package com.banking.loans.service;

public interface LoansService
{
	/**
	 * Creates new Loan based on mobile number.
	 *
	 * @param mobileNumber String
	 */
	void createLoan(final String mobileNumber);
}
