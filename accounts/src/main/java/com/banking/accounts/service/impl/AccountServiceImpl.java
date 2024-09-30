package com.banking.accounts.service.impl;

import com.banking.accounts.constants.AccountConstants;
import com.banking.accounts.dto.AccountsDto;
import com.banking.accounts.dto.CustomerDto;
import com.banking.accounts.entity.Accounts;
import com.banking.accounts.entity.Customer;
import com.banking.accounts.exception.CustomerAlreadyExistsException;
import com.banking.accounts.exception.ResourceNotFoundException;
import com.banking.accounts.mapper.AccountsMapper;
import com.banking.accounts.mapper.CustomerMapper;
import com.banking.accounts.repository.AccountsRepository;
import com.banking.accounts.repository.CustomerRepository;
import com.banking.accounts.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService
{
	private final AccountsRepository accountsRepository;
	private final CustomerRepository customerRepository;

	@Override
	public void createAccount(final CustomerDto customerDto)
	{
		final Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

		if (customerRepository.findByMobileNumber(customer.getMobileNumber()).isPresent())
		{
			throw new CustomerAlreadyExistsException(
					String.format("Customer with mobile number %s already exists", customerDto.getMobileNumber()));
		}

		customer.setCreatedAt(LocalDateTime.now());
		customer.setCreatedBy("Anonymous");
		final Customer savedCustomer = customerRepository.save(customer);
		final Accounts newAccount = accountsRepository.save(createNewAccount(savedCustomer));

		if (log.isDebugEnabled())
		{
			log.trace("Created account with customer id {}, abd account number {}", savedCustomer.getCustomerId(),
					newAccount.getAccountNumber());
		}
	}

	/**
	 * Creates new banking account for given Customer.
	 * @param customer Customer
	 * @return Accounts
	 */

	private Accounts createNewAccount(final Customer customer)
	{
		final Accounts newAccount = new Accounts();
		newAccount.setCustomerId(customer.getCustomerId());
		final long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);

		newAccount.setAccountNumber(randomAccountNumber);
		newAccount.setAccountType(AccountConstants.SAVINGS);
		newAccount.setBranchAddress(AccountConstants.ADDRESS);
		newAccount.setCreatedAt(LocalDateTime.now());
		newAccount.setCreatedBy("Anonymous");
		return newAccount;
	}

	@Override
	public CustomerDto getCustomerByMobileNumber(final String mobileNumber)
	{
		final Customer customer = customerRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

		final Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

		final CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
		customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

		return customerDto;
	}

	@Override
	public boolean updateAccount(final CustomerDto customerDto)
	{
		boolean updated = false;
		final AccountsDto accountsDto = customerDto.getAccountsDto();
		if (accountsDto.getAccountNumber() != null)
		{
			Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
					() -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString()));


			AccountsMapper.mapToAccounts(accountsDto, accounts);
			accounts = accountsRepository.save(accounts);

			final Long customerId = accounts.getCustomerId();
			final Customer customer = customerRepository.findById(customerId).orElseThrow(
					() -> new ResourceNotFoundException("Customer", "customerId", customerId.toString()));
			CustomerMapper.mapToCustomer(customerDto, customer);
			customerRepository.save(customer);
			updated = true;

			if (log.isDebugEnabled())
			{
				log.trace("Account with account nuumber {} has been successfully updated", accounts.getAccountNumber());
			}
		}
		return updated;
	}

	@Override
	public boolean deleteAccount(final String mobileNumber)
	{
		final Customer customer = customerRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

		customerRepository.deleteById(customer.getCustomerId());

		if (log.isDebugEnabled())
		{
			log.trace("Customer with mobile number {} has been deleted", mobileNumber);
		}

		accountsRepository.deleteByCustomerId(customer.getCustomerId());

		if (log.isDebugEnabled())
		{
			log.trace("Account with customer id {} has been deleted", customer.getCustomerId());
		}

		return true;
	}


}
