package com.tenx.ms.retail.store.exception;

import java.util.NoSuchElementException;

public class StoreNotExistsException extends NoSuchElementException {

	private static final long serialVersionUID = 1L;

	public StoreNotExistsException(String message) {
		super(message);
	}
}
