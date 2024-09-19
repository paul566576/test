package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.AccountTransactions;
import com.banking.secured_banking_app.models.Customer;
import com.banking.secured_banking_app.repositories.AccountTransactionsRepository;
import com.banking.secured_banking_app.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class BalanceController
{
	private final AccountTransactionsRepository accountTransactionsRepository;
	private final CustomerRepository customerRepository;


	@GetMapping("/myBalance")
	public List<AccountTransactions> getBalanceDetails(final @RequestParam String email)
	{
		final Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
		if (optionalCustomer.isPresent())
		{
			final List<AccountTransactions> accountTransactions = accountTransactionsRepository.
					findByCustomerIdOrderByTransactionDateDesc(optionalCustomer.get().getId());
			if (accountTransactions != null)
			{
				return accountTransactions;
			}
		}
		return null;
	}
}
