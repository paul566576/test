package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Loans;
import com.banking.secured_banking_app.repositories.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class LoansController
{

	private final LoanRepository loanRepository;

	@GetMapping("/myLoans")
	public List<Loans> getLoanDetails(final @RequestParam long id) {

		final List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(id);

		if (!CollectionUtils.isEmpty(loans)) {
			return loans;
		}
		return Collections.emptyList();
	}
}
