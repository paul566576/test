package com.banking.cards.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDto
{
	@NotEmpty
	private String cardNumber;
	@NotEmpty
	private String cardType;
	@NotEmpty
	private int totalLimit;
	@NotEmpty
	private int amountUsed;
	@NotEmpty
	private int availableAmount;
}
