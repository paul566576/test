package com.banking.loans.repository;

import com.banking.loans.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LoansRepository extends JpaRepository<Loan, Long>
{
	Optional<Loan> findByLoanNumber(final String loanNumber);

	Optional<Loan> findByMobileNumber(final String mobileNumber);
}
