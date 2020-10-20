package beertech.becks.api.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import beertech.becks.api.entities.Account;

public interface AccountRepository extends MongoRepository<Account, String> {
	Boolean existsByCode(String code);

	Optional<Account> findByCode(String code);
}
