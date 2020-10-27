package beertech.becks.consumer.services;

import beertech.becks.consumer.tos.messages.DepositWithdrawalMessage;
import beertech.becks.consumer.tos.messages.StatementsMessage;
import beertech.becks.consumer.tos.messages.TransferMessage;

/**
 * Interface describing the consumer services
 */
public interface ConsumerService {
	void treatDepositMessage(DepositWithdrawalMessage message);

	void treatWithdrawalMessage(DepositWithdrawalMessage message);

	void treatTransferMessage(TransferMessage message);

	String treatStatementsMessage(StatementsMessage message);
}
