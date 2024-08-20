package com.banking.secured_banking_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accounts
{
	@Column(name = "customer_id")
	private long customer_id;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_number")
	private long accountNumber;
	@Column(name = "account_type")
	private String accountType;
	@Column(name = "create_at")
	private Date createdAt;


}
