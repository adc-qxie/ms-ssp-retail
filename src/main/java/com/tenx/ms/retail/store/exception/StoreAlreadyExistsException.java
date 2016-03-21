package com.tenx.ms.retail.store.exception;

public class StoreAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public StoreAlreadyExistsException(String message) {
		super(message);
	}
}
