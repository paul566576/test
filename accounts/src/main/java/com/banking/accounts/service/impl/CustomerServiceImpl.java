package com.banking.accounts.service.impl;

import com.banking.accounts.dto.CardDto;
import com.banking.accounts.dto.CustomerDetailsDto;
import com.banking.accounts.dto.CustomerDto;
import com.banking.accounts.dto.LoanDto;
import com.banking.accounts.mapper.CustomerDetailsMapper;
import com.banking.accounts.service.AccountService;
import com.banking.accounts.service.CustomerService;
import com.banking.accounts.service.client.CardFeignClient;
import com.banking.accounts.service.client.LoansFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService
{
	private final AccountService accountService;
	private final CardFeignClient cardFeignClient;
	private final LoansFeignClient loansFeignClient;

	@Override
	public CustomerDetailsDto fetchCustomerDetailsByMobileNumber(final String mobileNumber)
	{
		final CustomerDto customerDto = accountService.getCustomerByMobileNumber(mobileNumber);
		final CardDto cardDto = cardFeignClient.fetchCardByMobileNumber(mobileNumber).getBody();

		if (log.isDebugEnabled())
		{
			log.debug("Fetch card details by mobile number: {}", mobileNumber);
		}

		final LoanDto loanDto = loansFeignClient.fetchLoanByMobileNumber(mobileNumber).getBody();

		if (log.isDebugEnabled())
		{
			log.debug("Fetch loan details by mobile number: {}", mobileNumber);
		}

		final CustomerDetailsDto customerDetailsDto = CustomerDetailsMapper.mapToCustomerDetailsDto(customerDto,
				new CustomerDetailsDto());
		customerDetailsDto.setCardDto(cardDto);
		customerDetailsDto.setLoanDto(loanDto);

		return customerDetailsDto;
	}
}
