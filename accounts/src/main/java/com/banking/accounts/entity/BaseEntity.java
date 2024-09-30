package com.banking.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseEntity
{
	@Column(updatable = false)
	private LocalDateTime createdAt;
	@Column(insertable = false)
	private LocalDateTime updatedAt;
	@Column(updatable = false)
	private String createdBy;
	@Column(insertable = false)
	private Long updatedBy;
}
