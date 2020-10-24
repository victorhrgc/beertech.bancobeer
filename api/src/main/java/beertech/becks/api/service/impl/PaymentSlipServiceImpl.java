package beertech.becks.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.Bank;
import beertech.becks.api.exception.payment.PaymentNotDoneException;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.repositories.BankRepository;
import beertech.becks.api.tos.response.PaymentResponseTO;
import beertech.becks.api.tos.response.PaymentSlipResponseTO;
import beertech.becks.api.tos.response.PaymentSlipUserTO;
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
	private BankRepository bankRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private RabbitProducer rabbitProducer;

	@Override
	public List<PaymentSlipResponseTO> findAll() {
		return convertToPaymentSlipResponseTO(paymentSlipRepository.findAll());
	}

	@Override
	public List<PaymentSlipResponseTO> findByUserId(Long userId) throws UserDoesNotExistException {
		if (!userRepository.existsById(userId)) {
			throw new UserDoesNotExistException();
		}

		return convertToPaymentSlipResponseTO(paymentSlipRepository.findByUserId(userId));
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

	private List<PaymentSlipResponseTO> convertToPaymentSlipResponseTO(List<PaymentSlip> paymentSlips) {
		List<PaymentSlipResponseTO> ret = new ArrayList<>();

		Optional<Bank> becksBank = bankRepository.findByCode("001");

		paymentSlips.forEach(slip -> {

			Optional<Bank> destinationBank = bankRepository.findByCode(slip.getDestinationBankCode());

			// Monta usuario origem
			PaymentSlipUserTO originUser = PaymentSlipUserTO.builder().userName(slip.getUser().getName())
					.accountCode(slip.getOriginAccountCode()).build();
			becksBank.ifPresent(bank -> originUser.setBankName(bank.getName()));

			// Monta usuario destino
			PaymentSlipUserTO destinationUser = PaymentSlipUserTO.builder()
					.accountCode(slip.getDestinationAccountCode()).build();
			destinationBank.ifPresent(bank -> destinationUser.setBankName(bank.getName()));

			if (slip.getDestinationBankCode().equals("001")) {
				Optional<Account> destinationUserAccount = accountRepository
						.findByCode(slip.getDestinationAccountCode());
				destinationUserAccount.ifPresent(account -> destinationUser.setUserName(account.getUser().getName()));
			}

			ret.add(PaymentSlipResponseTO.builder().code(slip.getCode()).dueDate(slip.getDueDate())
					.value(slip.getValue()).id(slip.getId()).originUser(originUser).destinationUser(destinationUser)
					.category(slip.getCategory()).build());

		});
		return ret;
	}
}
