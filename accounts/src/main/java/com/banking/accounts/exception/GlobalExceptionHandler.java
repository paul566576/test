package com.banking.accounts.exception;

import com.banking.accounts.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;


@ControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(value = { CustomerAlreadyExistsException.class })
	public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(final CustomerAlreadyExistsException exception,
			final WebRequest request)
	{
		final ErrorResponseDto errorResponseDto = new ErrorResponseDto(
				request.getDescription(false),
				HttpStatus.BAD_REQUEST,
				exception.getMessage(),
				LocalDateTime.now()
		);

		return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(final ResourceNotFoundException exception,
			final WebRequest request)
	{
		final ErrorResponseDto errorResponseDto = new ErrorResponseDto(
				request.getDescription(false),
				HttpStatus.NOT_FOUND,
				exception.getMessage(),
				LocalDateTime.now()
		);

		return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
	}
}
