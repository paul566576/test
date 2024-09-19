package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Cards;
import com.banking.secured_banking_app.models.Customer;
import com.banking.secured_banking_app.repositories.CardsRepository;
import com.banking.secured_banking_app.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class CardController
{
	private final CardsRepository cardsRepository;
	private final CustomerRepository customerRepository;


	@GetMapping("/myCards")
	public List<Cards> getCardDetails(final @RequestParam String email)
	{
		final Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
		if (optionalCustomer.isPresent())
		{
			final List<Cards> cards = cardsRepository.findByCustomerId(optionalCustomer.get().getId());
			if (cards != null)
			{
				return cards;
			}
		}
		return Collections.emptyList();
	}
}
