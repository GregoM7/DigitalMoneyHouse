package com.digitalmoney.cardservice.entity;


import lombok.Data;

@Data
public class Account {

    private Integer id;
    private Integer userId;
    private int balance;

    public Account(Integer userId, int balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }

    public Account() {
    }

    public Account(Integer id, Integer userId, int balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }
}