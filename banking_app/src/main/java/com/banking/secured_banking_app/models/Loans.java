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
public class Loans
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loan_number")
	private long loanNumber;
	@Column(name="customer_id")
	private long customerId;
	@Column(name = "start_dt")
	private Date startDt;
	@Column(name = "loan_type")
	private String loanType;
	@Column(name="total_loan")
	private int totalLoan;
	@Column(name = "amount_paid")
	private int amountPaid;
	@Column(name = "outstanding_amount")
	private int outstandingAmount;
	@Column(name = "create_at")
	private Date createAt;

}
