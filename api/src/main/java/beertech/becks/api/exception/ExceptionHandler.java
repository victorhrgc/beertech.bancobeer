package beertech.becks.api.exception;

import beertech.becks.api.exception.user.UserAlreadyExistsException;
import beertech.becks.api.exception.user.UserDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.transaction.InvalidTransactionOperationException;
import beertech.becks.api.tos.response.ErrorResponseTO;

@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		FieldError error = ex.getBindingResult().getFieldErrors().get(0);

		return new ResponseEntity<>(new ErrorResponseTO(error.getDefaultMessage()), HttpStatus.BAD_REQUEST);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(AccountDoesNotExistsException.class)
	public ResponseEntity<ErrorResponseTO> handleAccountDoesNotExistsException(AccountDoesNotExistsException ex) {
		return new ResponseEntity<>(new ErrorResponseTO("Account does not exist"), HttpStatus.NOT_FOUND);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(AccountAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseTO> handleAccountAlreadyExistsException(AccountAlreadyExistsException ex) {
		return new ResponseEntity<>(new ErrorResponseTO("Account with code " + ex.getMessage() + " already exists"),
				HttpStatus.OK);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(InvalidTransactionOperationException.class)
	public ResponseEntity<ErrorResponseTO> handleInvalidTransactionOperationException(
			InvalidTransactionOperationException ex) {
		return new ResponseEntity<>(new ErrorResponseTO("Operation " + ex.getMessage() + " is invalid"), HttpStatus.OK);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(UserDoesNotExistException.class)
	public ResponseEntity<ErrorResponseTO> handleUserDoesNotExistException(UserDoesNotExistException ex) {
		return new ResponseEntity<>(new ErrorResponseTO("User does not exist"), HttpStatus.NOT_FOUND);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseTO> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
		return new ResponseEntity<>(new ErrorResponseTO("User already exists"), HttpStatus.OK);
	}

}
