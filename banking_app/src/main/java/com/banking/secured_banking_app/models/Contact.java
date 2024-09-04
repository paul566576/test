package com.banking.secured_banking_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact_messages")
public class Contact
{
	@Id
	@Column(name = "contact_id")
	private String contactId;
	@Column(name = "contact_name")
	private String contactName;
	@Column(name = "contact_email")
	private String contactEmail;
	@Column(name = "subject")
	private String subject;
	@Column(name = "message")
	private String message;
	@Column(name = "create_dt")
	private Date createAt;

}
