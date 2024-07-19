package com.banking.secured_banking_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NoticeController
{

	@GetMapping("/notices")
	public String getMyNotices() {
		return "My Notices";
	}
}
