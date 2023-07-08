package com.digitalmoney.accountservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class TransactionFeignRepositoryTest {

    @Autowired
    private ITransactionFeignRepository transactionFeignRepository;

    @Test
    public void testGetLastFiveByAccountId() {
        Integer accountId = 1;
        List<Transaction> transactions = transactionFeignRepository.getLastFiveByAccountId(accountId);
        assertNotNull(transactions);
        assertEquals(5, transactions.size());
    }

    @Test
    public void testGetByAccountAndTransactionId() {
        Integer accountId = 1;
        Integer transactionId = 123;
        Transaction transaction = transactionFeignRepository.getByAccountAndTransactionId(accountId, transactionId);
        assertNotNull(transaction);
        assertEquals(accountId, transaction.getAccountId());
        assertEquals(transactionId, transaction.getTransactionId());
    }

    @Test
    public void testGetAllByAccountId() {
        Integer accountId = 1;
        List<Transaction> transactions = transactionFeignRepository.getAllByAccountId(accountId);
        assertNotNull(transactions);
    }

    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction();
        Transaction createdTransaction = transactionFeignRepository.createTransaction(transaction);
        assertNotNull(createdTransaction);
    }
}
