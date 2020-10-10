package beertech.becks.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import beertech.becks.api.entities.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Boolean existsByCode(String code);

	Optional<Account> findByCode(String code);
}
