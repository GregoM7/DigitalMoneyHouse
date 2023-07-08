package com.digitalmoney.transactionservice.exception;

public class BadRequestException extends Exception{
    public BadRequestException(String message){
        super(message);
    }
}