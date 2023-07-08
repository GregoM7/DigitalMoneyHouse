package com.digitalmoney.accountservice.repository.feign;

import com.digitalmoney.accountservice.entity.AccountsList;
import com.digitalmoney.accountservice.entity.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "transaction-service", url = "localhost:8093/transactions")
public interface ITransactionFeignRepository {
    @GetMapping("/accountId/lastFive/{accountId}")
    List<Transaction> getLastFiveByAccountId(@PathVariable(name = "accountId") Integer accountId);
    @GetMapping("/{transactionId}/{accountId}")
    Transaction getByAccountAndTransactionId(@PathVariable(name = "accountId") Integer accountId, @PathVariable(name = "transactionId") Integer transactionId);
    @GetMapping("/accountId/{accountId}")
    List<Transaction> getAllByAccountId(@PathVariable(name = "accountId") Integer accountId);
    @PostMapping("/transaction/")
    Transaction createTransaction(@RequestBody Transaction transaction);
    @GetMapping("/lastAccounts/{accountId}")
    List<Transaction> getLastAccount(@PathVariable(name = "accountId") String accountId);
}
