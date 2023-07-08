package com.digitalmoney.transactionservice.exception;

public class AuthenticationException extends Exception{
    public AuthenticationException(String message){
        super(message);
    }
}