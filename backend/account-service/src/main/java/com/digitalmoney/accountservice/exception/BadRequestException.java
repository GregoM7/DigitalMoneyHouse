package com.digitalmoney.accountservice.exception;

public class BadRequestException extends Exception{
    public BadRequestException(String message){
        super(message);
    }
}
