package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Cards;
import com.banking.secured_banking_app.repositories.CardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class CardController
{
	private final CardsRepository cardsRepository;

	@GetMapping("/myCards")
	public List<Cards> getCardDetails(final @RequestParam long id)
	{
		final List<Cards> cards = cardsRepository.findByCustomerId(id);

		if (!CollectionUtils.isEmpty(cards))
		{
			return cards;
		}
		return Collections.emptyList();
	}
}
