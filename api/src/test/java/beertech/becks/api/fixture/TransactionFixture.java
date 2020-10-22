package beertech.becks.api.fixture;

import beertech.becks.api.entities.Transaction;

import static beertech.becks.api.constants.ConstantsTests.Values.VALUE_100;
import static beertech.becks.api.model.TypeOperation.DEPOSITO;
import static beertech.becks.api.utils.DateUtil.stringInLocalDateTime;

public class TransactionFixture {

    public static Transaction aTranscationDeposit() {
        return Transaction
                .builder()
                .accountId(1L)
                .dateTime(stringInLocalDateTime("20/10/2020 10:10:15"))
                .typeOperation(DEPOSITO)
                .valueTransaction(VALUE_100)
                .build();
    }

}
