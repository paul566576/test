package com.banking.secured_banking_app.repositories;

import com.banking.secured_banking_app.models.Loans;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LoanRepository extends CrudRepository<Loans, Long>
{
	List<Loans> findByCustomerIdOrderByStartDtDesc(final long  customerId);
}
