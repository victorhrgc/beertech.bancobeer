package beertech.becks.api.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import beertech.becks.api.entities.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

	List<Transaction> findByAccountId(String accountId);
}
