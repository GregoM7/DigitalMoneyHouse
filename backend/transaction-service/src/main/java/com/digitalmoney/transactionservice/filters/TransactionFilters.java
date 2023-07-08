package com.digitalmoney.transactionservice.filters;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
public class TransactionFilters {

    private Double amountMin;
    private Integer idAccount;
    private Double amountMax;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private String type;


    public boolean hasType(){  return type != null; }
    public boolean hasAmount(){
       return amountMin != null && amountMax != null;
   }
    public boolean hasFechaDesde() {
        return fechaDesde != null;
    }
    public boolean hasFechaHasta() {
        return fechaHasta != null;
    }
    public boolean hasidAccount() {return idAccount != null;}
}
