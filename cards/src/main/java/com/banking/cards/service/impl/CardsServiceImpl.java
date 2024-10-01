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


@Service
@RequiredArgsConstructor
@Slf4j
public class CardsServiceImpl implements CardsService
{
	private final CardsRepository cardsRepository;

	@Override
	public void createCard(final CardDto cardDto)
	{
		final Card card = CardMapper.mapToCard(cardDto, new Card());

		if (cardsRepository.findByCardNumber(cardDto.getCardNumber()).isPresent())
		{
			throw new CardAlreadyExistsException(String.format(
					CardsConstants.CARD_ALREADY_EXISTS_MESSAGE, cardDto.getCardNumber()));
		}

		cardsRepository.save(card);
		if (log.isDebugEnabled())
		{
			log.trace("New Card with cardNumber: {} has been created", card.getCardNumber());
		}
	}

	@Override
	public List<CardDto> fetchCardsByMobileNumber(final String mobileNumber)
	{
		final List<Card> cards = cardsRepository.findByMobileNumber(mobileNumber);

		if (cards.isEmpty())
		{
			throw new ResourceNotFoundException("Card", "mobileNumber", mobileNumber);
		}

		final List<CardDto> cardDtos = new ArrayList<>();

		cards.forEach(card -> {
			cardDtos.add(CardMapper.mapToCardDto(card, new CardDto()));

			if (log.isDebugEnabled())
			{
				log.trace("Card with cardNumber: {} has been successfully fetched", card.getCardNumber());
			}
		});

		return cardDtos;
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
	public boolean deleteCard(final String cardNumber)
	{
		final Card card = cardsRepository.findByCardNumber(cardNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardNumber));

		cardsRepository.deleteById(card.getCardId());

		if (log.isDebugEnabled())
		{
			log.trace("Card with cardNumber: {} has been successfully deleted", card.getCardNumber());
		}
		return true;
	}


}
