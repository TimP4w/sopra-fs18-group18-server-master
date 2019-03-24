package ch.uzh.ifi.seal.soprafs18.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class GenericResource {

	Logger myLogger = LoggerFactory.getLogger(GenericResource.class);

	@ExceptionHandler(TransactionSystemException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public void handleTransactionSystemException(Exception exception) {
		myLogger.error("", exception);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleException(Exception exception) {
		myLogger.error("", exception);
	}

	@ExceptionHandler(ResourceException.class)
	public ResponseEntity handleException(ResourceException e) {
        myLogger.error(e.getMessage());
		return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
	}
}