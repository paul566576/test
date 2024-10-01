package com.banking.cards.service;

import com.banking.cards.dto.CardDto;

import java.util.List;


public interface CardsService
{
	/**
	 * Creates new Card based on CardDto data.
	 *
	 * @param cardDto CardDto
	 */
	void createCard(final CardDto cardDto);

	/**
	 * Fetch all cards with given MobileNumber.
	 *
	 * @param mobileNumber String
	 */
	List<CardDto> fetchCardsByMobileNumber(final String mobileNumber);

	/**
	 * Fetch card with given CardNumber.
	 *
	 * @param cardNumber String
	 */
	CardDto fetchCardsByCardNumber(final String cardNumber);

	/**
	 *
	 * @param cardDto CardDto
	 * @return boolean indicating if the update of Card details ıs successful or not
	 */
	boolean updateCard(final CardDto cardDto);

	/**
	 *
	 * @param cardNumber String
	 * @return boolean indicating if the delete of Card details ıs successful or not
	 */
	boolean deleteCard(final String cardNumber);
}
