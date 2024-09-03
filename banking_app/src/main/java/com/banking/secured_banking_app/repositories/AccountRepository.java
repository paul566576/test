package com.banking.secured_banking_app.repositories;

import com.banking.secured_banking_app.models.Accounts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends CrudRepository<Accounts, Long>
{
	Accounts findByCustomerId(final long customerId);
}
