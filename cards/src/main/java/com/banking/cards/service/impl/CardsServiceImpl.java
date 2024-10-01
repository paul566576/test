package com.banking.cards.service.impl;

import com.banking.cards.constants.CardsConstants;
import com.banking.cards.dto.CardDto;
import com.banking.cards.entity.Card;
import com.banking.cards.exception.CardAlreadyExistsException;
import com.banking.cards.mapper.CardMapper;
import com.banking.cards.repository.CardsRepository;
import com.banking.cards.service.CardsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


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

		cardsRepository.findByCardNumber(cardDto.getCardNumber()).orElseThrow(() -> new CardAlreadyExistsException(String.format(
				CardsConstants.CARD_ALREADY_EXISTS_MESSAGE, cardDto.getCardNumber())));

		cardsRepository.save(card);
		if (log.isDebugEnabled())
		{
			log.trace("New Card with cardNumber: {} has been created", card.getCardNumber());
		}
	}
}
