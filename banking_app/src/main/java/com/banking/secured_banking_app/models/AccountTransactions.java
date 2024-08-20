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
public class AccountTransactions
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private String transactionId;
	@Column(name = "account_number")
	private long accountNumber;
	@Column(name = "customer_id")
	private long customerId;
	@Column(name = "transaction_at")
	private Date transactionDate;
	@Column(name = "transaction_sumamry")
	private String transactionSummary;
	@Column(name = "transaction_type")
	private String transactionType;
	@Column(name = "transaction_amt")
	private int transactionAmount;
	@Column(name = "trransaction_balamce")
	private int closingBalance;
	@Column(name = "create_at")
	private Date createAt;


}
