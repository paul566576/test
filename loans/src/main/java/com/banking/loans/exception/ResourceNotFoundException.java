package com.banking.loans.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException
{

	public ResourceNotFoundException(final String resourseName, final String fieldName, final String fieldValue)
	{
		super(String.format("Resource %s not found for given input data %s: %s", resourseName, fieldName, fieldValue));
	}
}
