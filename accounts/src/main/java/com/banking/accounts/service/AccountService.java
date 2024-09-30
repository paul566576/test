package com.banking.accounts.service;

import com.banking.accounts.dto.CustomerDto;


public interface AccountService
{

	/**
	 * Create Customer entry save it to DB.
	 *
	 * @param customerDto
	 */
	void createAccount(final CustomerDto customerDto);


	/**
	 * Get Customer from DB for given mobileNumber.
	 * In case when Customer with given mobileNumber is not found
	 *
	 * @param mobileNumber
	 * @T
	 */
	CustomerDto getCustomerByMobileNumber(final String mobileNumber);
}
