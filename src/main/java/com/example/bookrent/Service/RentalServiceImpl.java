package com.example.bookrent.Service;

import com.example.bookrent.Converter.Converter;
import com.example.bookrent.Dto.RentalDto;
import com.example.bookrent.Entity.Book;
import com.example.bookrent.Entity.Rental;
import com.example.bookrent.Entity.Wallet;
import com.example.bookrent.Repository.BookRepo;
import com.example.bookrent.Repository.RentalRepo;
import com.example.bookrent.Repository.WalletRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements RentalService{
    private final RentalRepo rentalRepo;
    private final WalletRepo walletRepo;
    private final BookRepo bookRepo;

    public RentalServiceImpl(RentalRepo rentalRepo, WalletRepo walletRepo, BookRepo bookRepo) {
        this.rentalRepo = rentalRepo;
        this.walletRepo = walletRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    public String rentBook(Long userId, Long bookId, int days) {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        Wallet wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Wallet not found"));

        double cost = book.getRentingPrice() * days;
        if (wallet.getBalance() < cost) {
            return "Insufficient balance";
        }

        wallet.setBalance(wallet.getBalance() - cost);
        walletRepo.save(wallet);

        Rental rental = new Rental();
        rental.setBookId(bookId);
        rental.setUserId(userId);
        rental.setRentalStartDate(LocalDate.now());
        rental.setRentalEndDate(LocalDate.now().plusDays(days));
        rental.setIsReturned(false);
        rentalRepo.save(rental);

        return "Book rented successfully";
    }

    @Override
    public String extendRental(Long rentalId, int extraDays) {
        Rental rental = rentalRepo.findById(rentalId).orElseThrow(() -> new RuntimeException("Rental not found"));
        Book book = bookRepo.findById(rental.getBookId()).orElseThrow(() -> new RuntimeException("Book not found"));
        Wallet wallet = walletRepo.findByUserId(rental.getUserId()).orElseThrow(() -> new RuntimeException("Wallet not found"));

        double cost = book.getRentingPrice() * extraDays;
        if (wallet.getBalance() < cost) {
            return "Insufficient balance to extend rental";
        }

        wallet.setBalance(wallet.getBalance() - cost);
        walletRepo.save(wallet);

        rental.setRentalEndDate(rental.getRentalEndDate().plusDays(extraDays));
        rentalRepo.save(rental);

        return "Rental period extended successfully";
    }

    @Override
    public String returnBook(Long rentalId) {
        Rental rental = rentalRepo.findById(rentalId).orElseThrow(() -> new RuntimeException("Rental not found"));
        rental.setIsReturned(true);
        rentalRepo.save(rental);
        return "Book returned successfully";
    }

    @Override
    public List<RentalDto> getAllRentals() {
        List<Rental> rentals = rentalRepo.findAll();
        return rentals.stream()
                .map(this::convertTo)
                .collect(Collectors.toList());
    }
    private RentalDto convertTo(Rental rental) {
//        return new RentalDto(
//                rental.getId(),
//                rental.getBookId(),
//                rental.getUserId(),
//                rental.getRentalStartDate(),
//                rental.getRentalEndDate(),
//                rental.getIsReturned()
//        );
        return Converter.convertToDto(rental,RentalDto.class);
    }
    private Rental convertToEntity(RentalDto rentalDto){
        return Converter.convertToEntity(rentalDto,Rental.class);
    }
}
