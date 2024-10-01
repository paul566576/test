package com.banking.cards.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDto
{
	@NotEmpty
	@NotEmpty(message = "CardNumber can't be null or empty")
	@Size(min = 16, max = 16, message = "The length of the caardNumber always must be 16")
	private String cardNumber;
	@NotEmpty(message = "MobileNumber can't be null or empty")
	@Pattern(regexp = "(^$|[0-9]{11})", message = "Mobile number must be 11 digits")
	private String mobileNumber;
	@NotEmpty(message = "CardType can't be null or empty")
	private String cardType;
	@NotEmpty(message = "TotalLimit can't be null or empty")
	private int totalLimit;
	@NotEmpty(message = "AmountUsed can't be null or empty")
	private int amountUsed;
	@NotEmpty(message = "AvailableAmount can't be null or empty")
	private int availableAmount;
}
