package com.digitalmoney.transactionservice.repository;

import com.digitalmoney.transactionservice.entity.Transaction;
import com.digitalmoney.transactionservice.repository.custom.CustomTransactionRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>, CustomTransactionRepository {

    @Query(" FROM transactions WHERE id = ?1 AND (accountOriginId = ?2 OR accountDestinyId = ?2)")
    Transaction getByAccountAndTransactionId(Integer transactionId, Integer accountId);

    @Query(" FROM transactions WHERE (accountOriginId = ?1 OR accountDestinyId = ?1)")
    List<Transaction> getAllByAccountId(Integer accountId);

    @Query(value = " FROM transactions WHERE (accountOriginId = ?1 OR accountDestinyId = ?1) ORDER BY date DESC")
    List<Transaction> getLastTransactionByAccountId(Integer accountId, Pageable pageable);
}