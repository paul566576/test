package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Customer;
import com.banking.secured_banking_app.models.Loans;
import com.banking.secured_banking_app.repositories.CustomerRepository;
import com.banking.secured_banking_app.repositories.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class LoansController
{

	private final LoanRepository loanRepository;
	private final CustomerRepository customerRepository;


	@GetMapping("/myLoans")
	public List<Loans> getLoanDetails(final @RequestParam String email)
	{

		final Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);

		if (optionalCustomer.isPresent())
		{
			final List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(optionalCustomer.get().getId());
			if (loans != null)
			{
				return loans;
			}
		}
		return Collections.emptyList();
	}
}
