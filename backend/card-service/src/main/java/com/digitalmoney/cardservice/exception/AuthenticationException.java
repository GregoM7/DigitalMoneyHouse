package com.digitalmoney.cardservice.exception;

public class AuthenticationException extends Exception{
    public AuthenticationException(String message){
        super(message);
    }
}