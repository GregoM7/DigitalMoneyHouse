package com.digitalmoney.accountservice.service;

import com.digitalmoney.accountservice.entity.*;
import com.digitalmoney.accountservice.exception.BadRequestException;
import com.digitalmoney.accountservice.exception.ResourceNotFoundException;
import com.digitalmoney.accountservice.repository.AccountRepository;
import com.digitalmoney.accountservice.repository.feign.ITransactionFeignRepository;
import com.digitalmoney.accountservice.repository.feign.IUserFeignRepository;
import com.digitalmoney.accountservice.service.interfaces.IAccountService;
import com.sun.jersey.api.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ITransactionFeignRepository transactionFeignRepository;

    @Autowired
    private IUserFeignRepository userFeignRepository;

    public AccountService(AccountRepository accountRepository, ITransactionFeignRepository transactionFeignRepository, IUserFeignRepository userFeignRepository) {
        this.accountRepository = accountRepository;
        this.transactionFeignRepository = transactionFeignRepository;
        this.userFeignRepository = userFeignRepository;
    }


    @Override
    public Optional<Account> getAccountDetailsById(Integer id) throws Exception{

        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()){
            User userFeign = userFeignRepository.getCvcAndAliasByUserId(account.get().getUserId());
            User user = new User(account.get().getUserId(), userFeign.getCvu(), userFeign.getAlias());
            account.get().setUser(user);
            return account;
        }else {
            throw new BadRequestException("No se encontro cuenta con este id");
        }
    }

    public List<Transaction> getActivityById(Integer id) throws Exception{

        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()){
            List<Transaction> transactionsFeign = transactionFeignRepository.getAllByAccountId(id);
            return transactionsFeign;
        }else {
            throw new ResourceNotFoundException("No se encontro cuenta con este id");
        }
    }

    @Override
    public Optional<List<Transaction>> getLastFiveByAccountId(Integer accountId) throws BadRequestException, ResourceNotFoundException {
        List<Transaction> transactions = transactionFeignRepository.getLastFiveByAccountId(accountId);

        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron transferencias con este id de cuenta");
        }

        return Optional.of(transactions);
    }

    public Transaction createTransaction(Transaction transaction) throws ResourceNotFoundException {
        try{
            Transaction transaction1 = transactionFeignRepository.createTransaction(transaction);
            return transaction1;
        }catch (Exception e){
            throw new ResourceNotFoundException("No se pudo crear la transferencia");
        }
    }


    @Override
    public Optional<Account> createAccount(Integer userId){
        Account newAccount = new Account(userId, 0);
        accountRepository.save(newAccount);
        return Optional.of(newAccount);
    }

    @Override
    public Optional<Transaction> getByAccountAndTransactionId(Integer accountId, Integer transactionId) throws BadRequestException, ResourceNotFoundException {
        Transaction transactionfeign = transactionFeignRepository.getByAccountAndTransactionId(accountId,transactionId);
        if (transactionfeign.getId() == null) {
            throw new ResourceNotFoundException("No se encontraron transferencias con este id de cuenta");
        }
        return Optional.of(transactionfeign);
    }

    public Account updateAccount(Account account){
        return accountRepository.save(account);
    }

    public List<Transaction> getLastTransferencesByAccountId(String id) throws BadRequestException, ResourceNotFoundException {

        List<Transaction> lista  = transactionFeignRepository.getLastAccount(id);

        if (lista == null) {
            throw new ResourceNotFoundException("No se encontraton cuentas transferidas con este id");
        }

        return lista;
    }

    public List<AccountDestiny> getLastAccountId(Integer id) throws BadRequestException, ResourceNotFoundException {


        List<Object> lista= accountRepository.getLastAccountList(id);
        List<AccountDestiny> lv_accountDestiny = new ArrayList<>();
        for (Object obj:lista) {
        Object[] objArray = (Object[]) obj;

          AccountDestiny acc = new AccountDestiny();
          acc.setAccountDestinyID(Integer.valueOf(objArray[0].toString()));
          acc.setDate(Timestamp.valueOf(objArray[1].toString()));
          acc.setCvu(objArray[2].toString());
          acc.setCBU(objArray[3].toString());
          acc.setLastName(objArray[4].toString());
          acc.setName(objArray[5].toString());

          lv_accountDestiny.add(acc);
        }

        if (lv_accountDestiny.size() == 0) {
            throw new ResourceNotFoundException("No se encontraton cuentas transferidas con este id");
        }

        return lv_accountDestiny;
    }
}
