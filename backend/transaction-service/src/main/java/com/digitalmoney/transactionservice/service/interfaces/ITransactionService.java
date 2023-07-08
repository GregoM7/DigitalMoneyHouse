package com.digitalmoney.transactionservice.service.interfaces;

import com.digitalmoney.transactionservice.dto.AccountsList;
import com.digitalmoney.transactionservice.dto.PaginaDTO;
import com.digitalmoney.transactionservice.entity.Account;
import com.digitalmoney.transactionservice.entity.Transaction;
import com.digitalmoney.transactionservice.exception.BadRequestException;
import com.digitalmoney.transactionservice.exception.ResourceNotFoundException;
import com.digitalmoney.transactionservice.filters.TransactionFilters;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ITransactionService {
    List<Transaction> getAll();
    Optional<Transaction> getById(Integer id);
    Optional<Transaction> getByAccountAndTransactionId(Integer accountId, Integer transactionId) throws BadRequestException, ResourceNotFoundException;
    Optional<List<Transaction>> getAllByAccountId(Integer id) throws BadRequestException, ResourceNotFoundException;
   // AccountsList getLastFiveByAccountId(Integer id) throws BadRequestException, ResourceNotFoundException;
    Transaction create(Transaction transaction);
    Transaction update(Transaction transaction);
    void delete(Integer id);
    Optional<Account> getAccountById(Integer accountOriginId);
    void updateAccount(Account account);
    PaginaDTO findByFilters(TransactionFilters filtros, Integer page, Integer size);
    void generateVoucher(Integer idTransaction) throws BadRequestException, IOException;
    List<Transaction> getLastByAccountId(Integer accountId)throws BadRequestException, ResourceNotFoundException;

}