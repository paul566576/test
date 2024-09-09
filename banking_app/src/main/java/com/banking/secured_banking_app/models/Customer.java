package com.banking.secured_banking_app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "pwd")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	@Column(name = "mobile_number")
	private String mobileNumber;
	@Column(name = "role")
	private String role;
	@Column(name = "create_at")
	@JsonIgnore
	private Date createAt;
	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Authority> authorities;
}
