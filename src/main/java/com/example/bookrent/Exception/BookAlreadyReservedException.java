package com.example.bookrent.Exception;

public class BookAlreadyReservedException extends RuntimeException{
    public BookAlreadyReservedException(String message){
        super(message);
    }
}