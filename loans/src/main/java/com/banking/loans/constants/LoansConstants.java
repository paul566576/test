package com.banking.loans.constants;

public class LoansConstants
{

	private LoansConstants()
	{
		// restrict instantiation
	}

	public static final String LOAN_ALREADY_EXISTS_MESSAGE = "Loan with number %s already exists";
	public static final String LOAN_WITH_MOBILE_NUMBER_ALREADY_EXISTS_MESSAGE = "Loan with mobile number %s already exists";
	public static final String HOME_LOAN = "Home Loan";
	public static final int NEW_LOAN_LIMIT = 1_00_000;
	public static final String STATUS_201 = "201";
	public static final String MESSAGE_201 = "Loan created successfully";
	public static final String STATUS_200 = "200";
	public static final String MESSAGE_200 = "Request processed successfully";
	public static final String STATUS_417 = "417";
	public static final String MESSAGE_417_UPDATE = "Update operation failed. Please try again or contact Dev team";
	public static final String MESSAGE_417_DELETE = "Delete operation failed. Please try again or contact Dev team";
}
