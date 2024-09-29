package com.authserver.service;

import com.authserver.model.Customer;
import com.authserver.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BankingAppUserDetailsService implements UserDetailsService
{

	private final CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException
	{
		final Customer customer = customerRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(
						String.format("User with given email %s wasn't found", username)));

		final List<GrantedAuthority> grantedAuthorities = customer.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getName()))
				.collect(Collectors.toUnmodifiableList());
		return new User(customer.getEmail(), customer.getPassword(), grantedAuthorities);
	}
}
