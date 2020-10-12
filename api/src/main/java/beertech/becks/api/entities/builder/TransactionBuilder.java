package beertech.becks.api.entities.builder;

import beertech.becks.api.entities.Transaction;
import beertech.becks.api.model.TypeOperation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public final class TransactionBuilder implements Builder<Transaction> {

    private Transaction transaction = new Transaction();

    private TransactionBuilder() {
    }

    public static TransactionBuilder aTransaction() {
        return new TransactionBuilder();
    }

    public TransactionBuilder withId(Long id) {
        this.transaction.setId(id);
        return this;
    }

    public TransactionBuilder withOperation(TypeOperation operation) {
        this.transaction.setTypeOperation(operation);
        return this;
    }

    public TransactionBuilder withValueTransaction(BigDecimal valueTransaction) {
        this.transaction.setValueTransaction(valueTransaction);
        return this;
    }

    public TransactionBuilder withDateTime(LocalDateTime dateTime) {
        this.transaction.setDateTime(dateTime);
        return this;
    }

    @Override
    public Transaction builder() {
        return this.transaction;
    }
}
