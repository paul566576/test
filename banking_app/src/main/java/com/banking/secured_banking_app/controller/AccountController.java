package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Accounts;
import com.banking.secured_banking_app.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AccountController
{
	private final AccountRepository accountRepository;

	@GetMapping("/myAccount")
	public Accounts getAccountDetails(final @RequestParam long id)
	{
		final Accounts accounts = accountRepository.findByCustomerId(id);

		if (accounts != null)
		{
			return accounts;
		}
		return null;
	}
}
