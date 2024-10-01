package com.banking.accounts.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accounts extends BaseEntity
{
	@Id
	private Long accountNumber;
	@NotEmpty
	private Long customerId;
	@NotEmpty
	private String accountType;
	@NotEmpty
	private String branchAddress;

}
