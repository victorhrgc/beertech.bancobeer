package beertech.becks.api.fixture;

import beertech.becks.api.entities.Transaction;

import java.time.LocalDateTime;

import static beertech.becks.api.constants.ConstantsTests.Values.VALUE_100;
import static beertech.becks.api.model.TypeOperation.DEPOSITO;


public class TransactionFixture {

    public static Transaction aTranscationDeposit() {
        return Transaction
                .builder()
                .accountId(1L)
                .dateTime(LocalDateTime.now())
                .typeOperation(DEPOSITO)
                .valueTransaction(VALUE_100)
                .build();
    }

}
