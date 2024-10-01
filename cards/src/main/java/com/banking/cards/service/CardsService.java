package com.banking.cards.service;

import com.banking.cards.dto.CardDto;

import java.util.List;


public interface CardsService
{
	/**
	 * Creates new Card based on mobile number.
	 *
	 * @param mobileNumber String
	 */
	void createCard(final String mobileNumber);

	/**
	 * Fetch all cards with given MobileNumber.
	 *
	 * @param mobileNumber String
	 */
	CardDto fetchCardsByMobileNumber(final String mobileNumber);

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
	 * @param mobileNumber String
	 * @return boolean indicating if the delete of Card details ıs successful or not
	 */
	boolean deleteCard(final String mobileNumber);
}
