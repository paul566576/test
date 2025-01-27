package com.banking.cards;

import com.banking.cards.dto.CardsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableDiscoveryClient
@EnableConfigurationProperties(CardsContactInfoDto.class)
@OpenAPIDefinition(info = @Info(title = "Cards microservice REST API Documentation",
		description = "Banking Cards microservice REST API Documentation",
		version = "v1",
		contact = @Contact(
				name = "Pavlo Boldienkov",
				email = "p.boldienkov@aimprosoft.com",
				url = "http//localhost:9000"
		),
		license = @License(
				name = "Apache 2.0",
				url = "http//localhost:9000"
		)
),
		externalDocs = @ExternalDocumentation(
				description = "Banking Cards microservice REST API Documentation",
				url = "http//localhost:9000/swagger-ui.html"
		)
)
public class CardsApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(CardsApplication.class, args);
	}

}
