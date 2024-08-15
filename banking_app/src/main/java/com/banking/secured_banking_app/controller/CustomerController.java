package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Customer;
import com.banking.secured_banking_app.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class CustomerController
{
	@Autowired
	private final CustomerRepository customerRepository;
	@Autowired
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody final Customer customer)
	{

		try
		{
			final String hashPwd = passwordEncoder.encode(customer.getPwd());
			customer.setPwd(hashPwd);
			final Customer savedCustomer =  customerRepository.save(customer);

			if (savedCustomer.getId() > 0) {
				return ResponseEntity.status(HttpStatus.CREATED).body("Customer successfully registered");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Customer could not be registered");
			}
		}
		catch (final Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
