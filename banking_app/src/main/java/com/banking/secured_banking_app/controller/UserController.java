package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.constats.ApplicationConstants;
import com.banking.secured_banking_app.dto.LoginRequestDTO;
import com.banking.secured_banking_app.dto.LoginResponseDTO;
import com.banking.secured_banking_app.models.Customer;
import com.banking.secured_banking_app.repositories.CustomerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class UserController
{
	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final Environment environment;

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

	@PostMapping("/apiLogin")
	public ResponseEntity<LoginResponseDTO> apiLogin(@RequestBody final LoginRequestDTO loginRequest)
	{
		final Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(),
				loginRequest.password());
		final Authentication authenticationResponse = authenticationManager.authenticate(authentication);
		String jwt = null;
		if (null != authenticationResponse && authenticationResponse.isAuthenticated())
		{
			final String secret = environment.getProperty(ApplicationConstants.JWT_SECRET_KEY,
					ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
			final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
			jwt = Jwts.builder().issuer("Banking app").subject("JWT token")
					.claim("username", authenticationResponse.getName())
					.claim("authorities", authenticationResponse.getAuthorities().stream().map(GrantedAuthority::getAuthority)
							.collect(Collectors.joining("."))).issuedAt(new Date())
					.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)).signWith(secretKey).compact();
		}
		return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER, jwt)
				.body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
	}
}
