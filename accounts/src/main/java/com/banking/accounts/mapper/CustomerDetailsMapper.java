package com.banking.accounts.mapper;

import com.banking.accounts.dto.CustomerDetailsDto;
import com.banking.accounts.dto.CustomerDto;


public class CustomerDetailsMapper
{
	public static CustomerDetailsDto mapToCustomerDetailsDto(final CustomerDto source, final CustomerDetailsDto target) {
		target.setName(source.getName());
		target.setEmail(source.getEmail());
		target.setMobileNumber(source.getMobileNumber());
		target.setAccountsDto(source.getAccountsDto());
		return target;
	}
}
