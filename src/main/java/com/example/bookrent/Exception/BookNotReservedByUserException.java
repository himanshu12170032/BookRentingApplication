package com.example.bookrent.Exception;

public class BookNotReservedByUserException extends Exception {
    public BookNotReservedByUserException(String message) {
        super(message);
    }
}
