package com.banking.secured_banking_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CardController
{

	@GetMapping("/myCards")
	public String getMyCards() {
		return "My Cards";
	}
}
