package com.banking.loans.mapper;

import com.banking.loans.dto.LoanDto;
import com.banking.loans.entity.Loan;


public class LoanMapper{
	public static LoanDto mapToLoanDto(final Loan source, final LoanDto target)
	{
		target.setLoanType(source.getLoanType());
		target.setLoanNumber(source.getLoanNumber());
		target.setMobileNumber(source.getMobileNumber());
		target.setTotalLoan(source.getTotalLoan());
		target.setAmountPaid(source.getAmountPaid());
		target.setOutstandingAmount(source.getOutstandingAmount());
		return target;
	}

	public static Loan mapToLoan(final LoanDto source, final Loan target)
	{
		target.setLoanType(source.getLoanType());
		target.setLoanNumber(source.getLoanNumber());
		target.setMobileNumber(source.getMobileNumber());
		target.setTotalLoan(source.getTotalLoan());
		target.setAmountPaid(source.getAmountPaid());
		target.setOutstandingAmount(source.getOutstandingAmount());
		return target;
	}
}
