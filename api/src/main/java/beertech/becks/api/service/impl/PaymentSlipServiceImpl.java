package beertech.becks.api.service.impl;

import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
import beertech.becks.api.exception.payment.PaymentSlipExecutionException;
import beertech.becks.api.repositories.PaymentSlipRepository;
import beertech.becks.api.service.PaymentSlipService;
import beertech.becks.api.service.TransactionService;
import beertech.becks.api.tos.request.PaymentSlipRequestTO;
import beertech.becks.api.tos.request.TransferRequestTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class PaymentSlipServiceImpl implements PaymentSlipService {

	@Autowired
	private PaymentSlipRepository paymentSlipRepository;

	@Autowired
	private TransactionService transactionService;

	@Override
	public List<PaymentSlip> findAll() {
		return paymentSlipRepository.findAll();
	}

	@Override
	public Optional<PaymentSlip> findByUserDocumentNumber(String documentNumber) {
		return paymentSlipRepository.findByUserDocumentNumber(documentNumber);
	}

	@Override
	public PaymentSlip executePayment(PaymentSlipRequestTO paymentSlipRequestTO) throws PaymentSlipExecutionException {

		// Logica que vai decodificar o "codigo de barras" e vai retornar um PaymentSlip
		PaymentSlip paymentSlip = new PaymentSlip();

		try {

			if("Becks".equals(paymentSlip.getBank().getCode())) {
				// Se vazio, entao eh apenas um pagamento da conta atual
				if(paymentSlipRequestTO.getDestinationAccountCode().isEmpty()) {
					transactionService.createWithdrawal(paymentSlipRequestTO.getCurrentAccountCode(), paymentSlip.getValue());
				}
				else {
					TransferRequestTO transferRequestTO = new TransferRequestTO();
					transferRequestTO.setDestinationAccountCode(paymentSlipRequestTO.getDestinationAccountCode());
					transferRequestTO.setValue(paymentSlip.getValue());
					transactionService.createTransfer(paymentSlipRequestTO.getCurrentAccountCode(), transferRequestTO);
				}
			}
		}
		catch(AccountDoesNotExistsException | AccountDoesNotHaveEnoughBalanceException e) {
			throw new PaymentSlipExecutionException(e.getMessage()); // repensar essa forma de tratar a execao
		}

		return new PaymentSlip();
	}
}
