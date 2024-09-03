package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Contact;
import com.banking.secured_banking_app.repositories.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;


@RestController
@RequiredArgsConstructor
public class ContactController
{
	private final ContactRepository contactRepository;

	@GetMapping("/contacts")
	public Contact getMyContacts(final @RequestBody Contact contact)
	{
		contact.setContactId(getServiceNumber());
		contact.setCreateAt(new Date(System.currentTimeMillis()));
		return contactRepository.save(contact);
	}

	private String getServiceNumber() {
		final Random random = new Random();
		final   int ranNum = random.nextInt(999999999 - 99999) + 9999;
		return "SR" + ranNum;
	}
}
