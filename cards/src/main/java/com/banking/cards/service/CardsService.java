package com.banking.cards.service;

import com.banking.cards.dto.CardDto;


public interface CardsService
{
	/**
	 * Creates new Card based on CardDto data.
	 *
	 * @param cardDto CardDto
	 */
	void createCard(final CardDto cardDto);
}
