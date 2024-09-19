package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Accounts;
import com.banking.secured_banking_app.models.Customer;
import com.banking.secured_banking_app.repositories.AccountRepository;
import com.banking.secured_banking_app.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class AccountController
{
	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;

	@GetMapping("/myAccount")
	public Accounts getAccountDetails(final @RequestParam String email)
	{
		final Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
		if (optionalCustomer.isPresent())
		{
			final Accounts accounts = accountRepository.findByCustomerId(optionalCustomer.get().getId());
			if (accounts != null)
			{
				return accounts;
			}
		}
		return null;
	}
}
