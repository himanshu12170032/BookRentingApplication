package com.example.bookrent.Service;

import com.example.bookrent.Converter.Converter;
import com.example.bookrent.Dto.RentalDto;
import com.example.bookrent.Entity.Book;
import com.example.bookrent.Entity.Rental;
import com.example.bookrent.Entity.User;
import com.example.bookrent.Entity.Wallet;
import com.example.bookrent.Exception.ResourceNotFoundException;
import com.example.bookrent.Repository.BookRepo;
import com.example.bookrent.Repository.RentalRepo;
import com.example.bookrent.Repository.UserRepo;
import com.example.bookrent.Repository.WalletRepo;
import com.example.bookrent.Rules.RentalLimitRule;
import com.example.bookrent.Rules.RentalRuleStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements RentalService {
    private final RentalRepo rentalRepo;
    private final WalletRepo walletRepo;
    private final BookRepo bookRepo;
    private final UserRepo userRepo;

    public RentalServiceImpl(RentalRepo rentalRepo, WalletRepo walletRepo, BookRepo bookRepo, UserRepo userRepo) {
        this.rentalRepo = rentalRepo;
        this.walletRepo = walletRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    // Rent a Book
    @Override
    public String rentBook(Long userId, Long bookId, int days) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        if (wallet.getDebt() > 0) {
            return "You have a pending debt of $" + wallet.getDebt() +
                    ". Please clear it before renting another book.";
        }

        // Validate rental limits
        RentalLimitRule rule = RentalRuleStrategy.getRule(user.getRole());
        if (!validateRentalLimit(user)) {
            return "Rental limit exceeded for your user type.";
        }

        // Check max rental days
        if (days > rule.getMaxRentalDays()) {
            return "You cannot rent for more than " + rule.getMaxRentalDays() + " days.";
        }

        // Calculate discounted cost
        double cost = calculateRentalCost(book.getRentingPrice(), days, rule.getDiscount());
        if (wallet.getBalance() < cost) {
            return "Insufficient balance.";
        }

        // Deduct cost and save wallet
        wallet.setBalance(wallet.getBalance() - cost);
        walletRepo.save(wallet);

        // Save rental details
        Rental rental = new Rental();
        rental.setBook(book);
        rental.setUser(user);
        rental.setRentalStartDate(LocalDate.now());
        rental.setRentalEndDate(LocalDate.now().plusDays(days));
        rental.setIsReturned(false);
        rentalRepo.save(rental);

        return "Book rented successfully for " + days + " days!";
    }

    private double calculateRentalCost(double pricePerDay, int days, double discount) {
        return pricePerDay * days * (1 - discount); // Handles discounts in one place
    }


    @Override
    public String extendRental(Long rentalId, int extraDays) {
        Rental rental = rentalRepo.findById(rentalId).orElseThrow(() -> new RuntimeException("Rental not found"));
        Book book = bookRepo.findById(rental.getBook().getId()).orElseThrow(() -> new RuntimeException("Book not found"));
        Wallet wallet = walletRepo.findByUserId(rental.getUser().getId()).orElseThrow(() -> new RuntimeException("Wallet not found"));

        User user = userRepo.findById(rental.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
        double discount = RentalRuleStrategy.getRule(user.getRole()).getDiscount();

        double costPerDay = book.getRentingPrice();
        double additionalCost = costPerDay * extraDays * (1 - discount);

        if (wallet.getBalance() < additionalCost) {
            return "Insufficient balance to extend rental!";
        }

        wallet.setBalance(wallet.getBalance() - additionalCost);
        walletRepo.save(wallet);

        rental.setRentalEndDate(rental.getRentalEndDate().plusDays(extraDays));
        rentalRepo.save(rental);

        return "Rental extended by " + extraDays + " days! Additional Cost: $" + additionalCost;
    }


    // Return Book
    @Override
    public String returnBook(Long rentalId) {
        // Fetch rental details
        Rental rental = rentalRepo.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));

        // Check if already returned
        if (rental.getIsReturned()) {
            return "Book is already returned.";
        }

        // Fetch related data
        User user = rental.getUser();
        Wallet wallet = walletRepo.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        Book book = rental.getBook();

        // Rental details
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = rental.getRentalEndDate();
        LocalDate startDate = rental.getRentalStartDate();

        RentalLimitRule rule = RentalRuleStrategy.getRule(user.getRole());
        double discount = rule.getDiscount();
        double pricePerDay = book.getRentingPrice();

        // Calculate refund or penalty
        if (currentDate.isBefore(endDate)) {
            // Early return: Refund unused days
            long unusedDays = endDate.toEpochDay() - currentDate.toEpochDay();
            double refund = unusedDays * pricePerDay * (1 - discount);
            wallet.setBalance(wallet.getBalance() + refund); // Add refund to wallet
        } else if (currentDate.isAfter(endDate)) {
            // Late return: Add debt for extra days
            long overdueDays = currentDate.toEpochDay() - endDate.toEpochDay();
            double penalty = overdueDays * pricePerDay; // No discount on penalties
            wallet.setDebt(wallet.getDebt() + penalty); // Add debt
        }

        // Mark as returned
        rental.setIsReturned(true);
        rentalRepo.save(rental);
        walletRepo.save(wallet);

        // Return result
        return currentDate.isBefore(endDate) ?
                "Book returned early. Refund of $" + (pricePerDay * (1 - discount)) + " credited!" :
                "Book returned late. Penalty of $" + (pricePerDay) + " added to debt.";
    }


    // Get All Rentals
    @Override
    public List<RentalDto> getAllRentals() {
        List<Rental> rentals = rentalRepo.findAll();
        return rentals.stream()
                .map(this::convertTo)
                .collect(Collectors.toList());
    }

    private RentalDto convertTo(Rental rental) {
        return Converter.convertToDto(rental, RentalDto.class);
    }

    private Rental convertToEntity(RentalDto rentalDto) {
        return Converter.convertToEntity(rentalDto, Rental.class);
    }

    private boolean validateRentalLimit(User user) {
        RentalLimitRule rule = RentalRuleStrategy.getRule(user.getRole());
        long activeRentals = rentalRepo.countByUserIdAndIsReturned(user.getId(), false);
        return activeRentals < rule.getMaxRents(); // Active rental count validation
    }

    @Override
    public String payDebt(Long userId, double amount) {
        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        double debt = wallet.getDebt();
        if (debt == 0) {
            return "You have no pending debt.";
        }

        if (amount >= debt) {
            wallet.setDebt(0); // Clear debt
            wallet.setBalance(wallet.getBalance() + (amount - debt)); // Add remaining balance
        } else {
            wallet.setDebt(debt - amount); // Reduce debt
        }

        walletRepo.save(wallet);
        return "Debt payment successful. Remaining debt: $" + wallet.getDebt();
    }



}
