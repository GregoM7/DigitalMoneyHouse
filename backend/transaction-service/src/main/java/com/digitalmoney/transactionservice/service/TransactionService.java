package com.digitalmoney.transactionservice.service;

import com.digitalmoney.transactionservice.dto.AccountsList;
import com.digitalmoney.transactionservice.dto.PaginaDTO;
import com.digitalmoney.transactionservice.entity.Account;
import com.digitalmoney.transactionservice.entity.Transaction;
import com.digitalmoney.transactionservice.exception.BadRequestException;
import com.digitalmoney.transactionservice.exception.ResourceNotFoundException;
import com.digitalmoney.transactionservice.filters.TransactionFilters;
import com.digitalmoney.transactionservice.repository.TransactionRepository;
import com.digitalmoney.transactionservice.repository.custom.CustomPage;
import com.digitalmoney.transactionservice.repository.feign.IAccountFeignRepository;
import com.digitalmoney.transactionservice.service.interfaces.ITransactionService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {
    private TransactionRepository transactionRepository;
    private IAccountFeignRepository iAccountFeignRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, IAccountFeignRepository iAccountFeignRepository) {
        this.transactionRepository = transactionRepository;
        this.iAccountFeignRepository = iAccountFeignRepository;
    }

    @Override
    public List<Transaction> getAll() {
        return this.transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> getById(Integer id) {
        return this.transactionRepository.findById(id);
    }

    @Override
    public Optional<Transaction> getByAccountAndTransactionId(Integer accountId, Integer transactionId) throws BadRequestException, ResourceNotFoundException {

        Transaction transaction = transactionRepository.getByAccountAndTransactionId(transactionId,accountId);

        if (transaction == null) {
            throw new ResourceNotFoundException("No se encontro transferencia con este id");
        }

        return Optional.of(transaction);
    }

    @Override
    public Optional<List<Transaction>> getAllByAccountId(Integer id) throws BadRequestException, ResourceNotFoundException {

        List<Transaction> transactions = transactionRepository.getAllByAccountId(id);

        if (transactions == null) {
            throw new ResourceNotFoundException("No se encontraton transferencias con este id");
        }

        return Optional.of(transactions);
    }

    @Override
    public List<Transaction> getLastByAccountId(Integer id) throws BadRequestException, ResourceNotFoundException {

        List<Transaction> transactions = transactionRepository.getLastTransactionByAccountId(id, Pageable.ofSize(10));

        if (transactions == null) {
            throw new ResourceNotFoundException("No se encontraton cuentas con este id");
        }

        return transactions;
    }

    @Override
    public Transaction create(Transaction transaction) {
        return this.transactionRepository.save(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) {
        return this.transactionRepository.save(transaction);
    }

    @Override
    public void delete(Integer id) {
        this.transactionRepository.deleteById(id);
    }

    @Override
    public Optional<Account> getAccountById(Integer accountOriginId) {
        try {
            Account account = iAccountFeignRepository.getAccountById(accountOriginId);
            return Optional.of(account);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public void updateAccount(Account account) {
        iAccountFeignRepository.udpate(account, account.getId());
    }

    public PaginaDTO findByFilters(TransactionFilters filtros, Integer page, Integer size) {
        page=page==null?0:page;
        size=size==null?10:size;
        Pageable pageRequest = PageRequest.of(page,size);
        CustomPage<Transaction> paginaActivitys = transactionRepository.findByFilters(filtros,pageRequest);
        List<Transaction> listActivitys = paginaActivitys.getContent();
        long numeroActivitys = paginaActivitys.getTotalElements();
        return new PaginaDTO<>(page,size,numeroActivitys,listActivitys);
    }

    public void generateVoucher(Integer idTransaction) throws BadRequestException, IOException {
        Optional<Transaction> transaction = transactionRepository.findById(idTransaction);
        if (transaction.isEmpty()){
            throw new BadRequestException("La transaccion no esta disponible para generar un comprobante");
        }
        Document document = new Document();
        String userHome = System.getProperty("user.home");
        String desktopPath = userHome + File.separator + "Desktop";
        File desktopFolder = new File(desktopPath);
        desktopFolder.mkdirs();

        try {

            PdfWriter.getInstance(document,new FileOutputStream(desktopPath + File.separator+"comprobanteTransferencia.pdf"));
            document.open();
            voucherDetails(document);
            document.add(new Paragraph("ID Transferencia: "+ transaction.get().getId()));
            document.add(new Paragraph("Cuenta Origen: "+ transaction.get().getAccountOriginId()));
            document.add(new Paragraph("Cuenta Destino: "+ transaction.get().getAccountDestinyId()));
            document.add(new Paragraph("Fecha: "+ transaction.get().getDate()));
            document.add(new Paragraph("Monto: "+ transaction.get().getAmount()));
            document.add(new Paragraph("Detalle: "+ transaction.get().getDetail()));
            document.add(new Paragraph("Tipo: "+ transaction.get().getType()));
            document.close();

            System.out.println("El archivo PDF se ha generado correctamente.");
        }
        catch (DocumentException | FileNotFoundException e){
            e.printStackTrace();
        }

    }

    public void voucherDetails(Document document) throws DocumentException, IOException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Comprobante Transferencia", titleFont);
        document.add(title);
        ClassLoader classLoader = getClass().getClassLoader();
        String imagePath = classLoader.getResource("logo/LogoDigital.JPG").getPath();
        Image logo = Image.getInstance(imagePath);
        logo.scaleToFit(100,100);
        logo.setAbsolutePosition(document.right(100),document.top(25));
        document.add(logo);

    }

}