package beertech.becks.api.service.impl;

import java.util.List;

import beertech.becks.api.exception.payment.PaymentNotDoneException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beertech.becks.api.amqp.RabbitProducer;
import beertech.becks.api.converter.PaymentSlipConverter;
import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
import beertech.becks.api.exception.paymentslip.PaymentSlipDoesNotExistsException;
import beertech.becks.api.exception.user.UserDoesNotExistException;
import beertech.becks.api.repositories.PaymentSlipRepository;
import beertech.becks.api.repositories.UserRepository;
import beertech.becks.api.service.PaymentSlipService;
import beertech.becks.api.service.TransactionService;

@Service
public class PaymentSlipServiceImpl implements PaymentSlipService {

	@Autowired
	PaymentSlipConverter converter;

	@Autowired
	private PaymentSlipRepository paymentSlipRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private RabbitProducer rabbitProducer;

	@Override
	public List<PaymentSlip> findAll() {
		return paymentSlipRepository.findAll();
	}

	@Override
	public List<PaymentSlip> findByUserId(Long userId) throws UserDoesNotExistException {
		if (!userRepository.existsById(userId)) {
			throw new UserDoesNotExistException();
		}
		return paymentSlipRepository.findByUserId(userId);
	}

	@Override
	public void executePayment(String paymentCode) throws PaymentSlipDoesNotExistsException,
			AccountDoesNotHaveEnoughBalanceException, AccountDoesNotExistsException, PaymentNotDoneException {

		PaymentSlip paymentSlip = paymentSlipRepository.findByCode(paymentCode)
				.orElseThrow(PaymentSlipDoesNotExistsException::new);

		if ("001".equals(paymentSlip.getDestinationBankCode())) {
			transactionService.createPayment(paymentSlip);
		} else { // Banco externo
			if (rabbitProducer.produceBlockingMessageSuccessfully(paymentCode)) {
				transactionService.createPaymentToExternalBank(paymentSlip);
			} else {
				throw new PaymentNotDoneException();
			}
		}
	}

	@Override
	public void decodeAndSave(String code) throws Exception {
		PaymentSlip paymentSlip = converter.codeToPaymentSlip(code);
		paymentSlipRepository.save(paymentSlip);
	}
}
