package com.banking.secured_banking_app.repositories;

import com.banking.secured_banking_app.models.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>
{
	Optional<Customer> findByEmail(final String email);
}
