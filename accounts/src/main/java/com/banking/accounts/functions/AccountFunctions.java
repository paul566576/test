package com.banking.accounts.functions;

import com.banking.accounts.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Component
@Slf4j
public class AccountFunctions
{
	@Bean
	public Consumer<Long> updateCommunication(final AccountService accountService) {
		return accountNumber -> {
			log.info("Updating Communication status for account number: {}", accountNumber.toString());
			accountService.updateCommunicationStatus(accountNumber);
		};
	}
}
