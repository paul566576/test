package com.banking.secured_banking_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoansController
{

	@GetMapping("/myLoans")
	public String getMyLoans() {
		return "My Loanses";
	}
}
