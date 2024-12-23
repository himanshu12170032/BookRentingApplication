package com.example.bookrent.Exception;

public class DebtPendingException extends RuntimeException{
    public DebtPendingException(String message){
        super(message);
    }
}
