package com.banking.message.functions;

import com.banking.message.dto.AccountsMsgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;


@Configuration
public class MessageFunctions
{
	private static final Logger LOG = LoggerFactory.getLogger(MessageFunctions.class);

	@Bean
	public Function<AccountsMsgDto, AccountsMsgDto> email() {
		return accountsMsgDto -> {
			LOG.info("Sending email with details: " + accountsMsgDto.toString());
			//todo implement logic send email to given user
			return accountsMsgDto;
		};
	}

	@Bean
	public Function<AccountsMsgDto, Long> sms() {
		return accountsMsgDto -> {
			LOG.info("Sending sms with details: " + accountsMsgDto.toString());
			//todo implement logic send sms to given user
			return accountsMsgDto.accountNumber();
		};
	}
}
