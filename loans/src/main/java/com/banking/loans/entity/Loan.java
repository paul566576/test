package com.banking.loans.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Loan
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long LoanId;
	private String mobileNumber;
	private String loanNumber;
	private String loanType;
	private int totalLoan;
	private int amountPaid;
	private int outstandingAmount;
	@CreationTimestamp
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime updatedAt;
	@CreatedBy
	private String createdBy;
	@LastModifiedBy
	private String updatedBy;
}
