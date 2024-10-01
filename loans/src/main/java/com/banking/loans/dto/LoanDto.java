package com.banking.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(
		name = "Loan",
		description = "Schema to hold Loan information"
)
public class LoanDto
{
	@NotEmpty(message = "Mobile number can't be null or empty")
	@Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits")
	@Schema(description = "Mobile number of the customer", example = "48571932698")
	private String mobileNumber;
	@NotEmpty(message = "Loan number can't be null or empty")
	@Pattern(regexp = "(^$|[0-9]{12})", message = "Loan number must be 12 digits")
	@Schema(description = "Loan number of banking account", example = "566556655665")
	private String loanNumber;
	@NotEmpty(message = "Loan Type can't be null or empty")
	@Schema(description = "Card type of banking account", example = "Home Credit")
	private String loanType;
	@PositiveOrZero(message = "Total loan limit should be greater than zero")
	@Schema(description = "Total loan of the card", example = "9000")
	private int totalLoan;
	@PositiveOrZero(message = "Amount paid limit should be greater than zero")
	@Schema(description = "Amount Paid of the loan", example = "9000")
	private int amountPaid;
	@PositiveOrZero(message = "Outstanding amount limit should be greater than zero")
	@Schema(description = "Outstanding amount of the loan", example = "9000")
	private int outstandingAmount;
}
