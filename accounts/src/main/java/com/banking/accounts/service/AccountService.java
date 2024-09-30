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
}
