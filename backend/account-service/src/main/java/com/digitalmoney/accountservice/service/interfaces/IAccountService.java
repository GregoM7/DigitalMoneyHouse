package com.digitalmoney.accountservice.service.interfaces;

import com.digitalmoney.accountservice.entity.Account;
import com.digitalmoney.accountservice.entity.Transaction;
import com.digitalmoney.accountservice.exception.BadRequestException;
import com.digitalmoney.accountservice.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    Optional<Account> getAccountDetailsById(Integer id) throws Exception;

    Optional<List<Transaction>> getLastFiveByAccountId(Integer accountId) throws BadRequestException, ResourceNotFoundException;

    Optional<Account> createAccount(Integer userId);

    Optional<Transaction> getByAccountAndTransactionId( Integer accountId, Integer transactionId) throws BadRequestException, ResourceNotFoundException;

    Transaction createTransaction(Transaction transaction) throws ResourceNotFoundException;
}
