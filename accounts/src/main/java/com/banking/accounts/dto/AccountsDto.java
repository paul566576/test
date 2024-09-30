package com.banking.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;



@Data
public class AccountsDto
{
	@NotEmpty(message = "AccountNumber can't be null or empty")
	@Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 10 digits")
	private Long accountNumber;
	@NotEmpty(message = "AccountType can't be null or empty")
	private String accountType;
	@NotEmpty(message = "BranchAddress can't be null or empty")
	private String branchAddress;
}
