package com.banking.secured_banking_app.config;

import com.banking.secured_banking_app.models.Customer;
import com.banking.secured_banking_app.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService
{
	@Autowired
	private final CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException
	{
		final Customer customer = customerRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		return new User(customer.getEmail(), customer.getPassword(),
				customer.getAuthorities().stream().map(a -> new SimpleGrantedAuthority(a.getName())).collect(Collectors.toList()));
	}
}
