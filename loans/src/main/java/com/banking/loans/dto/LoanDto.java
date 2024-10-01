package com.banking.loans.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanDto
{
	@NotEmpty(message = "Mobile number can't be null or empty")
	@Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits")
	private String mobileNumber;
	@NotEmpty(message = "Loan number can't be null or empty")
	@Pattern(regexp = "(^$|[0-9]{12})", message = "Mobile number must be 12 digits")
	private String loanNumber;
	@NotEmpty(message = "Loan Type can't be null or empty")
	private String loanType;
	@PositiveOrZero(message = "Total loan limit should be greater than zero")
	private int totalLoan;
	@PositiveOrZero(message = "Amount paid limit should be greater than zero")
	private int amountPaid;
	@PositiveOrZero(message = "Outstanding amount limit should be greater than zero")
	private int outstandingAmount;
}
