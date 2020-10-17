package beertech.becks.api.repositories;

import beertech.becks.api.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountId(Long accountId);

    @Query(value = "SELECT * FROM TRANSACTION WHERE FK_ACCOUNT_ID = ?1 AND DATA_TRANSACTION BETWEEN ?2 AND ?3", nativeQuery = true)
    List<Transaction> getAccountStatementsByPeriod(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
}
