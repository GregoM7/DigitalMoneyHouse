package com.digitalmoney.accountservice.controller;

import com.digitalmoney.accountservice.entity.*;
import com.digitalmoney.accountservice.exception.BadRequestException;
import com.digitalmoney.accountservice.exception.ResourceNotFoundException;
import com.digitalmoney.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountDetailsById(@PathVariable Integer id) throws Exception{
        Account account = accountService.getAccountDetailsById(id).get();
        return new ResponseEntity(account, HttpStatus.OK);
    }

    @GetMapping("/{id}/activity")
    public ResponseEntity<Account> getActivity(@PathVariable Integer id) throws Exception{
        List <Transaction> lv_activity = accountService.getActivityById(id);
        return new ResponseEntity(lv_activity, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest createAccountRequest){
        return new ResponseEntity(accountService.createAccount(createAccountRequest.getUserId()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account, @PathVariable Integer id){
        return new ResponseEntity<>(accountService.updateAccount(account), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Account> patchAccount(@RequestBody Account account, @PathVariable Integer id){
        return new ResponseEntity<>(accountService.updateAccount(account), HttpStatus.OK);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getLastFiveByAccountId(@PathVariable(name = "id") Integer accountId) throws BadRequestException, ResourceNotFoundException {
        Optional<List<Transaction>> optionalTransactions = accountService.getLastFiveByAccountId(accountId);

        return optionalTransactions.map(transactions -> ResponseEntity.ok().body(transactions)).orElseGet(() -> (ResponseEntity<List<Transaction>>) ResponseEntity.notFound());

    }
    @PostMapping("/{id}/transferences")
    public ResponseEntity<Transaction> createTransaction(@PathVariable Integer id, @RequestBody Transaction transaction){
        try {
            Optional<Account> accountOrigin = accountService.getAccountDetailsById(transaction.getAccountOriginId());
            if(accountOrigin.isEmpty()){
                return new ResponseEntity( "No existe la cuenta de origen", HttpStatus.BAD_REQUEST);
            }
            Optional<Account> accountDestiny = accountService.getAccountDetailsById(transaction.getAccountDestinyId());
            if(accountDestiny.isEmpty()){
                return new ResponseEntity( "No existe la cuenta destino", HttpStatus.BAD_REQUEST);
            }
            if(accountOrigin.get().getBalance() < transaction.getAmount()){
                return new ResponseEntity( "Saldo insuficiente", HttpStatus.GONE);
            }

            Transaction transactionSucesss = accountService.createTransaction(transaction);
            return new ResponseEntity(transactionSucesss, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/transferences")
    public ResponseEntity<List<Transaction>> getLastTransferences(@PathVariable String id){
        try {
            List<Transaction> lv_transferences = accountService.getLastTransferencesByAccountId(id);
            return new ResponseEntity(lv_transferences, HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/lastAccounts")
    public ResponseEntity<List<AccountDestiny>> getAccounts(@PathVariable Integer id) throws Exception{
        List<AccountDestiny> lista = accountService.getLastAccountId(id);
        return new ResponseEntity(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}/activity/{transactionId}")
    public ResponseEntity<Transaction> TransactionsDetailsbyId(@PathVariable(name = "id") Integer accountId, @PathVariable(name = "transactionId") Integer transactionId) throws BadRequestException, ResourceNotFoundException{
        Optional<Transaction> optionalTransaction = accountService.getByAccountAndTransactionId(accountId,transactionId);
        return optionalTransaction.map(transactions -> ResponseEntity.ok().body(transactions)).orElseGet(() -> (ResponseEntity<Transaction>) ResponseEntity.notFound());
    }
}
