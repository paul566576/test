package com.banking.cards.service.impl;

import com.banking.cards.constants.CardsConstants;
import com.banking.cards.dto.CardDto;
import com.banking.cards.entity.Card;
import com.banking.cards.exception.CardAlreadyExistsException;
import com.banking.cards.exception.ResourceNotFoundException;
import com.banking.cards.mapper.CardMapper;
import com.banking.cards.repository.CardsRepository;
import com.banking.cards.service.CardsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Slf4j
public class CardsServiceImpl implements CardsService
{
	private final CardsRepository cardsRepository;

	@Override
	public void createCard(final String mobileNumber)
	{
		final Card card = createNewCard(mobileNumber);

		if (cardsRepository.findByCardNumber(card.getCardNumber()).isPresent())
		{
			throw new CardAlreadyExistsException(String.format(CardsConstants.CARD_ALREADY_EXISTS_MESSAGE, card.getCardNumber()));
		}

		if (cardsRepository.findByMobileNumber(card.getMobileNumber()).isPresent())
		{
			throw new CardAlreadyExistsException(
					String.format(CardsConstants.CARD_WITH_MOBILE_NUMBER_ALREADY_EXISTS_MESSAGE, card.getMobileNumber()));
		}

		cardsRepository.save(card);

		if (log.isDebugEnabled())
		{
			log.trace("New Card with cardNumber: {} has been created", card.getCardNumber());
		}
	}

	/**
	 * @param mobileNumber - Mobile Number of the Customer
	 * @return the new card details
	 */
	private Card createNewCard(final String mobileNumber)
	{
		Card newCard = new Card();
		long randomCardNumber = 1000000000000000L + new Random().nextLong(9000000000000L);
		newCard.setCardNumber(Long.toString(randomCardNumber));
		newCard.setMobileNumber(mobileNumber);
		newCard.setCardType(CardsConstants.CARD_TYPE);
		newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
		newCard.setAmountUsed(0);
		newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
		return newCard;
	}

	@Override
	public CardDto fetchCardsByMobileNumber(final String mobileNumber)
	{
		final Card card = cardsRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));

		if (log.isDebugEnabled())
		{
			log.trace("Card with cardNumber: {} has been successfully fetched", card.getCardNumber());
		}

		return CardMapper.mapToCardDto(card, new CardDto());
	}

	@Override
	public CardDto fetchCardsByCardNumber(final String cardNumber)
	{
		final Card card = cardsRepository.findByCardNumber(cardNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardNumber));

		if (log.isDebugEnabled())
		{
			log.trace("Card with cardNumber: {} has been successfully fetched", card.getCardNumber());
		}

		return CardMapper.mapToCardDto(card, new CardDto());
	}

	@Override
	public boolean updateCard(final CardDto cardDto)
	{
		final Card card = cardsRepository.findByCardNumber(cardDto.getCardNumber())
				.orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardDto.getCardNumber()));
		CardMapper.mapToCard(cardDto, card);

		cardsRepository.save(card);

		if (log.isDebugEnabled())
		{
			log.trace("Card with cardNumber: {} has been successfully updated", card.getCardNumber());
		}
		return true;
	}

	@Override
	public boolean deleteCard(final String mobileNumber)
	{
		final Card card = cardsRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));

		cardsRepository.deleteById(card.getCardId());

		if (log.isDebugEnabled())
		{
			log.trace("Card with mobileNumber: {} has been successfully deleted", card.getCardNumber());
		}
		return true;
	}


}
