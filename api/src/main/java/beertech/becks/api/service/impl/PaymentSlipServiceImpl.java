package beertech.becks.api.service.impl;

import beertech.becks.api.amqp.RabbitProducer;
import beertech.becks.api.entities.Bank;
import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
import beertech.becks.api.exception.payment.PaymentSlipExecutionException;
import beertech.becks.api.repositories.PaymentSlipRepository;
import beertech.becks.api.service.PaymentSlipService;
import beertech.becks.api.service.TransactionService;
import beertech.becks.api.tos.request.PaymentRequestTO;
import beertech.becks.api.tos.request.TransferRequestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentSlipServiceImpl implements PaymentSlipService {

	@Autowired
	private PaymentSlipRepository paymentSlipRepository;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private RabbitProducer rabbitProducer;

	@Override
	public List<PaymentSlip> findAll() {
		return paymentSlipRepository.findAll();
	}

	@Override
	public Optional<PaymentSlip> findByUserId(Long userId) {
		return paymentSlipRepository.findByUserId(userId);
	}

	@Override
	public PaymentSlip executePayment(PaymentRequestTO paymentRequestTO) throws PaymentSlipExecutionException {

		// Logica que vai decodificar o "codigo de barras" e vai retornar um PaymentSlip
		PaymentSlip paymentSlip = new PaymentSlip();
		paymentSlip.setBank(Bank.builder().code("NotBecks").id(1L).build());

		try {

			if("Becks".equals(paymentSlip.getBank().getCode())) {
				// Se vazio, entao eh apenas um pagamento da conta atual
				if(paymentRequestTO.getDestinationAccountCode().isEmpty()) {
					transactionService.createWithdrawal(paymentRequestTO.getCurrentAccountCode(), paymentSlip.getValue());
				} else {
					TransferRequestTO transferRequestTO = new TransferRequestTO();
					transferRequestTO.setDestinationAccountCode(paymentRequestTO.getDestinationAccountCode());
					transferRequestTO.setValue(paymentSlip.getValue());
					transactionService.createTransfer(paymentRequestTO.getCurrentAccountCode(), transferRequestTO);
				}
			} else {
				if(!rabbitProducer.produceBlockingMessageSuccessfully(paymentRequestTO)) {
					//TODO fazer rollback do debito
					System.out.println("Erro!");
				} else {
					//TODO deletar depois, apenas para testes
					System.out.println("Sucesso!");
				}
			}
		}
		catch(AccountDoesNotExistsException | AccountDoesNotHaveEnoughBalanceException e) {
			throw new PaymentSlipExecutionException(e.getMessage()); // repensar essa forma de tratar a execao
		}

		return paymentSlip;
	}
}
