package beertech.becks.api.exception.paymentslip;

public class PaymentSlipDoesNotExistsException extends Exception {

	public PaymentSlipDoesNotExistsException(String message) {
		super(message);
	}

	public PaymentSlipDoesNotExistsException() {
		super();
	}
}
