package com.banking.secured_banking_app.repositories;

import com.banking.secured_banking_app.models.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactRepository extends CrudRepository<Contact, Long>
{

}
