package com.banking.secured_banking_app.repositories;

import com.banking.secured_banking_app.models.Cards;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CardsRepository extends CrudRepository<Cards, Long>
{
	List<Cards> findByCustomerId(final long customerId);
}
