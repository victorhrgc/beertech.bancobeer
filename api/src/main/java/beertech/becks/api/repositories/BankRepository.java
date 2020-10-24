package beertech.becks.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import beertech.becks.api.entities.Bank;

public interface BankRepository extends JpaRepository<Bank, Long> {
    Boolean existsByCode(String code);
}
