package com.banking.loans;

import com.banking.loans.dto.LoansContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(LoansContactInfoDto.class)
@OpenAPIDefinition(info = @Info(title = "Loans microservice REST API Documentation",
		description = "Banking Loans microservice REST API Documentation",
		version = "v1",
		contact = @Contact(
				name = "Pavlo Boldienkov",
				email = "p.boldienkov@aimprosoft.com",
				url = "http//localhost:8090"
		),
		license = @License(
				name = "Apache 2.0",
				url = "http//localhost:8090"
		)
),
		externalDocs = @ExternalDocumentation(
				description = "Banking Loans microservice REST API Documentation",
				url = "http//localhost:8090/swagger-ui.html"
		)
)
public class LoansApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(LoansApplication.class, args);
	}

}
