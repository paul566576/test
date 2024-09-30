package com.banking.accounts.mapper;

import com.banking.accounts.dto.CustomerDto;
import com.banking.accounts.entity.Customer;


public class CustomerMapper
{
	public static Customer mapToCustomer(final CustomerDto source, final Customer target)
	{
		target.setName(source.getName());
		target.setEmail(source.getEmail());
		target.setMobileNumber(source.getMobileNumber());
		return target;
	}

	public static CustomerDto mapToCustomerDto(final Customer source, final CustomerDto target)
	{
		target.setName(source.getName());
		target.setEmail(source.getEmail());
		target.setMobileNumber(source.getMobileNumber());
		return target;
	}

}
