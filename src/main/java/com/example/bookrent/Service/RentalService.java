package com.example.bookrent.Service;

import com.example.bookrent.Dto.RentalDto;

import java.util.List;

public interface RentalService {
    String rentBook(Long userId, Long bookId, int days);
    String extendRental(Long rentalId, int extraDays);
    String returnBook(Long rentalId);
    List<RentalDto> getAllRentals();
    public String payDebt(Long userId, double amount);
}
