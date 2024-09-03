package com.banking.secured_banking_app.repositories;

import com.banking.secured_banking_app.models.AccountTransactions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountTransactionsRepository extends CrudRepository<AccountTransactions, Long>
{
	List<AccountTransactions> findByCustomerIdOrderByTransactionDateDesc(final long accountId);
}
