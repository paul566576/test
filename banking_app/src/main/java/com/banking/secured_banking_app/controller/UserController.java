package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Customer;
import com.banking.secured_banking_app.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class UserController
{
	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody final Customer customer)
	{
		try
		{
			final String hashPwd = passwordEncoder.encode(customer.getPassword());
			customer.setPassword(hashPwd);
			customer.setCreateAt(new Date(System.currentTimeMillis()));
			final Customer savedCustomer = customerRepository.save(customer);

			if (savedCustomer.getId() > 0)
			{
				return ResponseEntity.status(HttpStatus.CREATED).body("Given user details are successfully registered");
			}
			else
			{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed!");
			}
		}
		catch (final Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An exception occurred: " + e.getMessage());
		}
	}

	@RequestMapping("/user")
	public Customer getUserDetailsAfterLogin(final Authentication authentication)
	{
		final Optional<Customer> customer = customerRepository.findByEmail(authentication.getName());
		return customer.orElse(null);
	}
}
