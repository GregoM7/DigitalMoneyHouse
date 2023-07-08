package com.digitalmoney.transactionservice.controller;
import com.digitalmoney.transactionservice.dto.AccountsList;
import com.digitalmoney.transactionservice.dto.PaginaDTO;
import com.digitalmoney.transactionservice.entity.Account;
import com.digitalmoney.transactionservice.entity.Transaction;
import com.digitalmoney.transactionservice.exception.BadRequestException;
import com.digitalmoney.transactionservice.exception.ResourceNotFoundException;
import com.digitalmoney.transactionservice.filters.TransactionFilters;
import com.digitalmoney.transactionservice.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private ITransactionService transactionService;

    @Autowired
    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/test")
    public String getInfo() {
        return "Te has conectado al microservicio de Transactions.";
    }

    @GetMapping
    public ResponseEntity<PaginaDTO<Transaction>> findAll(
            @RequestParam(value = "idAccount", required = true)Integer idAccount,
            @RequestParam(value = "pagina", required = false)Integer page,
            @RequestParam(value = "tamanio", required = false)Integer size,
            @RequestParam(value = "amountmax", required = false)Double amountMax,
            @RequestParam(value = "amountmin", required = false)Double amountMin,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "fechaDesde", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd") String fechaDesde,
            @RequestParam(value = "fechaHasta", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd") String fechaHasta,
            HttpServletRequest request
    ) throws BadRequestException {

       if(idAccount==null){
            throw new BadRequestException("Debe ingresar numero de cuenta");
        }

        PaginaDTO<Transaction> paginaActivity;
        TransactionFilters filtros = new TransactionFilters();
        filtros.setIdAccount(idAccount);
        if(amountMin!= null && amountMax != null) {
            filtros.setAmountMin(amountMin);
            filtros.setAmountMax(amountMax);
        }
        if (type!=null) filtros.setType(type);
        if (fechaDesde!=null && fechaHasta!=null) {
            filtros.setFechaDesde(LocalDate.parse(fechaDesde, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            filtros.setFechaHasta(LocalDate.parse(fechaHasta, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        paginaActivity = transactionService.findByFilters(filtros,page,size);

        String url = request.getRequestURL().toString();
        paginaActivity.setUrlBase(url);

        return new ResponseEntity<>(paginaActivity, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Integer id){
        Optional<Transaction> optionalTransaction = transactionService.getById(id);
        return optionalTransaction.map(transaction -> ResponseEntity.ok().body(transaction)).orElseGet(() -> (ResponseEntity<Transaction>) ResponseEntity.notFound());
    }

    @GetMapping("/{transactionId}/{accountId}")
    public ResponseEntity<Transaction> getByAccountAndTransactionId(@PathVariable(name = "accountId") Integer accountId, @PathVariable(name = "transactionId") Integer transactionId) throws BadRequestException, ResourceNotFoundException {
        Optional<Transaction> optionalTransaction = transactionService.getByAccountAndTransactionId(accountId, transactionId);

        return optionalTransaction.map(transaction -> ResponseEntity.ok().body(transaction)).orElseGet(() -> (ResponseEntity<Transaction>) ResponseEntity.notFound());

    }

    @GetMapping("/accountId/{accountId}")
    public ResponseEntity<List<Transaction>> getAllByAccountId(@PathVariable(name = "accountId") Integer accountId) throws BadRequestException, ResourceNotFoundException {

        Optional<List<Transaction>> optionalTransactions = transactionService.getAllByAccountId(accountId);

        return optionalTransactions.map(transactions -> ResponseEntity.ok().body(transactions)).orElseGet(() -> (ResponseEntity<List<Transaction>>) ResponseEntity.notFound());

    }

  /*  @GetMapping("/lastFiveAccounts/{accountId}")
    public ResponseEntity<AccountsList> getLastFiveByAccountId(@PathVariable String accountId) throws BadRequestException, ResourceNotFoundException {

        AccountsList retAccountsList = transactionService.getLastFiveByAccountId(Integer.valueOf(accountId));

        return ResponseEntity.ok(retAccountsList);
    }*/

    @GetMapping("/lastAccounts/{accountId}")
    public ResponseEntity<List<Transaction>> getLastByAccountId(@PathVariable String accountId) throws BadRequestException, ResourceNotFoundException {

        List<Transaction> lv_transactions = transactionService.getLastByAccountId(Integer.valueOf(accountId));

        return ResponseEntity.ok(lv_transactions);
    }

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction){
        System.out.println("Entro");
        try {
            //transaction.setDate(LocalDate.from(LocalTime.now()));
            Optional<Account> accountOrigin = transactionService.getAccountById(transaction.getAccountOriginId());
            if (accountOrigin.isEmpty()) {
                return new ResponseEntity("No existe la cuenta de origen", HttpStatus.BAD_REQUEST);
            }
            Optional<Account> accountDestiny = transactionService.getAccountById(transaction.getAccountDestinyId());
            if (accountDestiny.isEmpty()) {
                return new ResponseEntity("No existe la cuenta destino", HttpStatus.BAD_REQUEST);
            }
            if (accountOrigin.get().getBalance() < transaction.getAmount()) {
                return new ResponseEntity("Saldo insuficiente", HttpStatus.GONE);
            }
            accountOrigin.get().setBalance((int) (accountOrigin.get().getBalance() - transaction.getAmount()));
            transactionService.updateAccount(accountOrigin.get());

            accountDestiny.get().setBalance((int) (accountDestiny.get().getBalance() + transaction.getAmount()));
            transactionService.updateAccount(accountDestiny.get());

            Transaction senderTransaction = transactionService.create(transaction);

            if (transaction.getType() == "ingreso") {
                transaction.setType("egreso");
            } else {
                transaction.setType("ingreso");
            }

            transactionService.create(transaction);

            return ResponseEntity.ok().body(senderTransaction);

        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> update(@RequestBody Transaction Transaction, @PathVariable Integer id){
        try {
            Optional<Transaction> transaction = transactionService.getById(id);
            if (transaction.isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            if (Transaction.getId() == null) {
                Transaction.setId(id);
            }
            return ResponseEntity.ok().body(transactionService.update(Transaction));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        transactionService.delete(id);
        return  ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/comprobante")
    public ResponseEntity generateV(@PathVariable Integer id) throws BadRequestException, IOException {
        transactionService.generateVoucher(id);
        return ResponseEntity.ok("Comprobante Descargado");
    }



}