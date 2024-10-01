package com.banking.loans.service.impl;

import com.banking.loans.constants.LoansConstants;
import com.banking.loans.entity.Loan;
import com.banking.loans.exception.LoanAlreadyExistsException;
import com.banking.loans.repository.LoansRepository;
import com.banking.loans.service.LoansService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@RequiredArgsConstructor
@Slf4j
public class LoansServiceImpl implements LoansService
{

	private final LoansRepository loansRepository;


	@Override
	public void createLoan(final String mobileNumber)
	{
		final Loan loan = createNewLoan(mobileNumber);

		if (loansRepository.findByLoanNumber(loan.getLoanNumber()).isPresent())
		{
			throw new LoanAlreadyExistsException(String.format(LoansConstants.LOAN_ALREADY_EXISTS_MESSAGE, loan.getLoanNumber()));
		}

		loansRepository.save(loan);

		if (log.isDebugEnabled())
		{
			log.trace("New Card with loanNumber: {} has been created", loan.getLoanNumber());
		}
	}

	/**
	 * @param mobileNumber - Mobile Number of the Customer
	 * @return the new loan details
	 */
	private Loan createNewLoan(String mobileNumber)
	{
		Loan newLoan = new Loan();
		long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
		newLoan.setLoanNumber(Long.toString(randomLoanNumber));
		newLoan.setMobileNumber(mobileNumber);
		newLoan.setLoanType(LoansConstants.HOME_LOAN);
		newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
		newLoan.setAmountPaid(0);
		newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
		return newLoan;
	}
}
