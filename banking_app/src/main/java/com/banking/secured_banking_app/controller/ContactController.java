package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Contact;
import com.banking.secured_banking_app.repositories.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@RestController
@RequiredArgsConstructor
public class ContactController
{
	private final ContactRepository contactRepository;

	@PostMapping("/contacts")
	@PreFilter("filterObject.contactName != 'Test'")
	public List<Contact> saveContactInquiryDetails(@RequestBody List<Contact> contacts)
	{
		final List<Contact> returnContacts = new ArrayList<>();
		if (!contacts.isEmpty())
		{
			final Contact contact = contacts.getFirst();
			contact.setContactId(getServiceNumber());
			contact.setCreateDt(new Date(System.currentTimeMillis()));
			final Contact savedContact = contactRepository.save(contact);
			returnContacts.add(savedContact);
		}
		return returnContacts;
	}

	private String getServiceNumber()
	{
		final Random random = new Random();
		final int ranNum = random.nextInt(999999999 - 99999) + 9999;
		return "SR" + ranNum;
	}
}
