package com.banking.accounts.constants;

import java.util.List;
import java.util.Map;


public class AccountConstants
{
	private AccountConstants()
	{
		// Restrict instantiation
	}

	public static final String JAVA_HOME = "JAVA_HOME";
	public static final String SAVINGS = "Savings";
	public static final String ADDRESS = "123 Main Street, New York";
	public static final String STATUS_201 = "201";
	public static final String MESSAGE_201 = "Account created successfully";
	public static final String STATUS_200 = "200";
	public static final String MESSAGE_200 = "Request processed successfully";
	public static final String STATUS_417 = "417";
	public static final String MESSAGE_417_UPDATE = "Update operation failed. Please try again or contact Dev team";
	public static final String MESSAGE_417_DELETE = "Delete operation failed. Please try again or contact Dev team";
	public static final String DEFAULT_BUILD_VERSION = "1.0";
	public static final String DEFAULT_JAVA_VERSION = "21";
	public static final String WELCOME_TO_BANKING_ACCOUNTS_DEFAULT_MESSAGE = "Welcome to Banking accounts related local APIs";
	public static final List<String> ON_CALL_SUPPORT_DEFAULT_PHONE_NUMBERS = List.of("(555) 555-1234", "(555) 523-1345");
	public static final Map<String, String> DEFAULT_CONTACT_DETAILS_DATA = Map.of("name", "John Doe - Developer", "email", "john@eazybank.com");
}
