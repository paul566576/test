package com.banking.accounts;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Locale;


@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(info = @Info(title = "Accounts microservice REST API Documentation",
		description = "Banking Accounts microservice REST API  Documentation",
		version = "v1",
		contact = @Contact(
				name = "Pavlo Boldienkov",
				email = "p.boldienkov@aimprosoft.com",
				url = "http//localhost:8080"
		),
		license = @License(
				name = "Apache 2.0",
				url = "http//localhost:8080"
		)
),
		externalDocs = @ExternalDocumentation(
				description = "Banking Accounts microservice REST API  Documentation",
				url = "http//localhost:8080/swagger-ui.html"
		)
)
public class AccountsApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(AccountsApplication.class, args);
	}

}
