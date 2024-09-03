package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.AccountTransactions;
import com.banking.secured_banking_app.repositories.AccountRepository;
import com.banking.secured_banking_app.repositories.AccountTransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class BalanceController
{
	private final AccountTransactionsRepository accountTransactionsRepository;

	@GetMapping("/myBalance")
	public List<AccountTransactions> getBalanceDetails(final @RequestParam long id)
	{
		final List<AccountTransactions> transactions = accountTransactionsRepository
				.findByCustomerIdOrderByTransactionDateDesc(id);
		if (!CollectionUtils.isEmpty(transactions)) {
			return transactions;
		}
		return Collections.emptyList();
	}
}
