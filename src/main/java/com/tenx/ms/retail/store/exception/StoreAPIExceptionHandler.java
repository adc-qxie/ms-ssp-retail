package com.tenx.ms.retail.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.tenx.ms.commons.rest.APIExceptionHandler;
import com.tenx.ms.commons.rest.SystemError;

@ControllerAdvice
public class StoreAPIExceptionHandler extends APIExceptionHandler {

	@ExceptionHandler(StoreAlreadyExistsException.class)
	protected ResponseEntity<Object> handleStoreAlreadyExistsException(StoreAlreadyExistsException ex, WebRequest request) {
		return new ResponseEntity<>(new SystemError("Store withe the same name already exists", HttpStatus.BAD_REQUEST.value(), ex), HttpStatus.BAD_REQUEST);
	}

}
