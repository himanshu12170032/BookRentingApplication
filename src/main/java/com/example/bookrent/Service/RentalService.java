package com.example.bookrent.Service;

import com.example.bookrent.Dto.RentalDto;
import com.example.bookrent.Exception.BookNotReservedByUserException;

import java.util.List;

public interface RentalService {
    String rentBook(Long userId, Long bookId, int days);
    String extendRental(Long rentalId, int extraDays) throws BookNotReservedByUserException;
    String returnBook(Long rentalId) throws BookNotReservedByUserException;
    List<RentalDto> getAllRentals();
    public String payDebt(Long userId, double amount);
}
