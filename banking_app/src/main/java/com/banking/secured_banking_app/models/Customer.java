package com.banking.secured_banking_app.models;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NonNull
	private String email;
	@NonNull
	private String pwd;
	@Column(name="role")
	@NonNull
	private String role;
}
