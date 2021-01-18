package com.rafaelgude.postsapi.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<Error> handleObjectNotFound(ObjectNotFoundException e) {
	    var error = new Error(HttpStatus.NOT_FOUND, e.getMessage());
	    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Error> handleConstraintViolation(TransactionSystemException e) {
        if (e.getRootCause() instanceof ConstraintViolationException) {
        	var ex = (ConstraintViolationException) e.getRootCause();
        	var error = new Error(HttpStatus.BAD_REQUEST, (ex.getConstraintViolations() != null && ex.getConstraintViolations().size() != 0) ? 
										                  ex.getConstraintViolations().stream().findFirst().get().getMessage() : 
										                  ex.getMessage());
			
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
}
