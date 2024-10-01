package com.banking.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;



@Data
@Schema(
		name = "Account",
		description = "Schema to hold  Account information"
)
public class AccountsDto
{
	@NotEmpty(message = "AccountNumber can't be null or empty")
	@Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 10 digits")
	@Schema(description = "Account number of banking account", example = "1234567891")
	private Long accountNumber;
	@NotEmpty(message = "AccountType can't be null or empty")
	@Schema(description = "Account type of banking account")
	private String accountType;
	@NotEmpty(message = "BranchAddress can't be null or empty")
	@Schema(description = "Banking branch address", example = "123, New-York Main Street")
	private String branchAddress;
}
