package com.digitalmoney.cardservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name="cards")
@Table(name="cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer accountId;
    private String expiration;
    private String number;
    private String name;
    private String cvv;

    public Card(Integer id, Integer accountId, String expiration, String number, String name, String cvv) {
        this.id = id;
        this.accountId = accountId;
        this.expiration = expiration;
        this.number = number;
        this.name = name;
        this.cvv = cvv;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Card() {
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", expiration='" + expiration + '\'' +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }
}