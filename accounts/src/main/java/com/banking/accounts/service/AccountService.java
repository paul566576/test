package com.banking.accounts.service;

import com.banking.accounts.dto.CustomerDto;


public interface AccountService
{

	/**
	 * Create Customer entry save it to DB.
	 *
	 * @param customerDto CustomerDto
	 */
	void createAccount(final CustomerDto customerDto);


	/**
	 * Get Customer from DB for given mobileNumber.
	 * In case when Customer with given mobileNumber is not found ResourceNotFoundException should be thrown
	 *
	 * @param mobileNumber string
	 * @return CustomerDto
	 */
	CustomerDto getCustomerByMobileNumber(final String mobileNumber);

	/**
	 *
	 * @param customerDto CustomerDTO
	 * @return boolean indicating if the update of Account details ıs successful or not
	 */
	boolean updateAccount(final CustomerDto customerDto);

	/**
	 *
	 * @param mobileNumber String
	 * @return boolean indicating if the deleting of Account details ıs successful or not
	 */
	boolean deleteAccount(final String mobileNumber);
}
