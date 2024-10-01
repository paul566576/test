package com.banking.cards.mapper;

import com.banking.cards.dto.CardDto;
import com.banking.cards.entity.Card;


public class CardMapper
{
	public static Card mapToCard(final CardDto source, final Card target)
	{
		target.setCardNumber(source.getCardNumber());
		target.setMobileNumber(source.getMobileNumber());
		target.setCardType(source.getCardType());
		target.setTotalLimit(source.getTotalLimit());
		target.setAmountUsed(source.getAmountUsed());
		target.setAvailableAmount(source.getAvailableAmount());
		return target;
	}

	public static CardDto mapToCardDto(final Card source, final CardDto target)
	{
		target.setCardNumber(source.getCardNumber());
		target.setMobileNumber(source.getMobileNumber());
		target.setCardType(source.getCardType());
		target.setTotalLimit(source.getTotalLimit());
		target.setAmountUsed(source.getAmountUsed());
		target.setAvailableAmount(source.getAvailableAmount());
		return target;
	}
}
