package com.digitalmoney.transactionservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Entity(name="transactions")
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer accountOriginId;
    private Integer accountDestinyId;
    private Double amount;
    private LocalDate date;
    private String detail;
    private String type;

}