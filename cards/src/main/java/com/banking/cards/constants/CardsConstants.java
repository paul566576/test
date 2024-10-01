package com.banking.cards.constants;

public class CardsConstants
{
	private CardsConstants() {

	}
	public static final String CARD_ALREADY_EXISTS_MESSAGE = "Card with number %s already exists";
	public static final String CARD_WITH_MOBILE_NUMBER_ALREADY_EXISTS_MESSAGE = "Card with mobile number %s already exists";
	public static final String CARD_TYPE = "CREDIT";
	public static final int  NEW_CARD_LIMIT = 1_00_000;
	public static final String STATUS_201 = "201";
	public static final String MESSAGE_201 = "Card created successfully";
	public static final String STATUS_200 = "200";
	public static final String MESSAGE_200 = "Request processed successfully";
	public static final String STATUS_417 = "417";
	public static final String MESSAGE_417_UPDATE = "Update operation failed. Please try again or contact Dev team";
	public static final String MESSAGE_417_DELETE = "Delete operation failed. Please try again or contact Dev team";
}
