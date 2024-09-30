package com.banking.accounts.repository;

import com.banking.accounts.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long>
{
	Optional<Accounts> findByCustomerId(final Long customerId);
}
