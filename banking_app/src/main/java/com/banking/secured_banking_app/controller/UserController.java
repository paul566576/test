package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Customer;
import com.banking.secured_banking_app.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class UserController
{
	private final CustomerRepository customerRepository;


	@RequestMapping("/user")
	public Customer getUserDetailsAfterLogin(final Authentication authentication)
	{
		final Optional<Customer> customer = customerRepository.findByEmail(authentication.getName());
		return customer.orElse(null);
	}
}
