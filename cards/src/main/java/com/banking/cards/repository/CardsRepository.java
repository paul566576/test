package com.banking.cards.repository;

import com.banking.cards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CardsRepository extends JpaRepository<Card, Long>
{
	Optional<Card> findByCardNumber(final String cardNumber);
}
