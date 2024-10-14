package com.banking.accounts.service;


import com.banking.accounts.dto.CustomerDetailsDto;


public interface CustomerService
{
	/**
	 * Get Customer from DB for given mobileNumber.
	 * Fetching CardDTO and LoanDto data from corresponding MS.
	 * Converting all the data to CustomerDetailsDto
	 * In case when Customer, Accounts or Loans  with given mobileNumber is not found ResourceNotFoundException should be thrown
	 *
	 * @param mobileNumber string
	 * @return CustomerDetailsDto
	 */
	CustomerDetailsDto fetchCustomerDetailsByMobileNumber(final String mobileNumber);
}
