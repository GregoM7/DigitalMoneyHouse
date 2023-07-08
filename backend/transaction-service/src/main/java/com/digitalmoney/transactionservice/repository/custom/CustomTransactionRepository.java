package com.digitalmoney.transactionservice.repository.custom;

import com.digitalmoney.transactionservice.entity.Transaction;
import com.digitalmoney.transactionservice.filters.TransactionFilters;
import org.springframework.data.domain.Pageable;

public interface CustomTransactionRepository {
    CustomPage<Transaction> findByFilters(TransactionFilters filtros, Pageable pageable);
}
