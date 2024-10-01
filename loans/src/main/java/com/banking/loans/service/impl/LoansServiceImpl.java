package com.banking.loans.service.impl;

import com.banking.loans.constants.LoansConstants;
import com.banking.loans.dto.LoanDto;
import com.banking.loans.entity.Loan;
import com.banking.loans.exception.LoanAlreadyExistsException;
import com.banking.loans.exception.ResourceNotFoundException;
import com.banking.loans.mapper.LoanMapper;
import com.banking.loans.repository.LoansRepository;
import com.banking.loans.service.LoansService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
			log.trace("New loan with loanNumber: {} has been created", loan.getLoanNumber());
		}
	}

	@Override
	public List<LoanDto> fetchLoansByMobileNumber(final String mobileNumber)
	{
		final List<Loan> loans = loansRepository.findByMobileNumber(mobileNumber);

		if (loans.isEmpty())
		{
			throw new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber);
		}

		final List<LoanDto> loansDto = new ArrayList<>();

		loans.forEach(loan -> {
			loansDto.add(LoanMapper.mapToLoanDto(loan, new LoanDto()));
			if (log.isDebugEnabled())
			{
				log.trace("Loan with loanNumber: {} has been fetched successfully", loan.getLoanNumber());
			}
		});
		return loansDto;
	}

	@Override
	public LoanDto fetchLoansByLoanNumber(final String loanNumber)
	{
		final Loan loan = loansRepository.findByLoanNumber(loanNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Loan", "LoanNumber", loanNumber));

		if (log.isDebugEnabled())
		{
			log.trace("Loan with loanNumber: {} has been fetched successfully", loan.getLoanNumber());
		}

		return LoanMapper.mapToLoanDto(loan, new LoanDto());
	}

	@Override
	public boolean updateLoan(final LoanDto loanDto)
	{
		final Loan loan = loansRepository.findByLoanNumber(loanDto.getLoanNumber())
				.orElseThrow(() -> new ResourceNotFoundException("Loan", "LoanNumber", loanDto.getLoanNumber()));

		LoanMapper.mapToLoan(loanDto, loan);

		loansRepository.save(loan);

		if (log.isDebugEnabled())
		{
			log.trace("Loan with loanNumber: {} has been successfully updated", loan.getLoanNumber());
		}
		return true;
	}

	@Override
	public boolean deleteLoan(final String loanNumber)
	{
		final Loan loan = loansRepository.findByLoanNumber(loanNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Loan", "LoanNumber", loanNumber));

		loansRepository.deleteById(loan.getLoanId());

		if (log.isDebugEnabled())
		{
			log.trace("Loan with LoanNumber: {} has been successfully deleted", loan.getLoanNumber());
		}
		return true;
	}


	/**
	 * @param mobileNumber - Mobile Number of the Customer
	 * @return the new loan details
	 */
	private Loan createNewLoan(final String mobileNumber)
	{
		final Loan newLoan = new Loan();
		final long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
		newLoan.setLoanNumber(Long.toString(randomLoanNumber));
		newLoan.setMobileNumber(mobileNumber);
		newLoan.setLoanType(LoansConstants.HOME_LOAN);
		newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
		newLoan.setAmountPaid(0);
		newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
		return newLoan;
	}


}
