package com.banking.accounts.service.impl;

import com.banking.accounts.dto.CardDto;
import com.banking.accounts.dto.CustomerDetailsDto;
import com.banking.accounts.dto.CustomerDto;
import com.banking.accounts.dto.LoanDto;
import com.banking.accounts.mapper.CustomerDetailsMapper;
import com.banking.accounts.service.AccountService;
import com.banking.accounts.service.CustomerService;
import com.banking.accounts.service.client.CardsFeignClient;
import com.banking.accounts.service.client.LoansFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService
{
	private final AccountService accountService;
	@Qualifier("loansFeignClient") // Explicitly specify the Feign client proxy
	private final LoansFeignClient loansFeignClient;
	@Qualifier("cardsFeignClient") // Explicitly specify the Feign client proxy
	private final CardsFeignClient cardsFeignClient;


	@Override
	public CustomerDetailsDto fetchCustomerDetailsByMobileNumber(final String mobileNumber, final String correlationId)
	{
		final CustomerDto customerDto = accountService.getCustomerByMobileNumber(mobileNumber);
		final ResponseEntity<CardDto> cardDtoResponseEntity = cardsFeignClient.fetchCardByMobileNumber(correlationId, mobileNumber);
		final ResponseEntity<LoanDto> loanDtoResponseEntity = loansFeignClient.fetchLoanByMobileNumber(correlationId, mobileNumber);
		final CustomerDetailsDto customerDetailsDto = CustomerDetailsMapper.mapToCustomerDetailsDto(customerDto,
				new CustomerDetailsDto());
		if (Objects.nonNull(cardDtoResponseEntity))
		{
			if (log.isDebugEnabled())
			{
				log.debug("Fetch card details by mobile number: {}", mobileNumber);
			}
			customerDetailsDto.setCardDto(cardDtoResponseEntity.getBody());
		}
		if (Objects.nonNull(loanDtoResponseEntity))
		{
			if (log.isDebugEnabled())
			{
				log.debug("Fetch loan details by mobile number: {}", mobileNumber);
			}
			customerDetailsDto.setLoanDto(loanDtoResponseEntity.getBody());
		}
		return customerDetailsDto;
	}
}
