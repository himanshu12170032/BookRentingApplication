package com.example.bookrent.Service;

import com.example.bookrent.Converter.Converter;
import com.example.bookrent.Dto.RentalDto;
import com.example.bookrent.Entity.Book;
import com.example.bookrent.Entity.Rental;
import com.example.bookrent.Entity.User;
import com.example.bookrent.Entity.Wallet;
import com.example.bookrent.Exception.BookAlreadyReservedException;
import com.example.bookrent.Exception.BookNotReservedByUserException;
import com.example.bookrent.Exception.DebtPendingException;
import com.example.bookrent.Exception.ResourceNotFoundException;
import com.example.bookrent.Repository.BookRepo;
import com.example.bookrent.Repository.RentalRepo;
import com.example.bookrent.Repository.UserRepo;
import com.example.bookrent.Repository.WalletRepo;
import com.example.bookrent.Rules.RentalLimitRule;
import com.example.bookrent.Rules.RentalRuleStrategy;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    @Override
    public String rentBook(Long userId, Long bookId, int days) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Optional<Rental> activeRental = rentalRepo.findByBookIdAndIsReturned(bookId, false);
        if (activeRental.isPresent()) {
            throw new BookAlreadyReservedException("Book is already rented. Please wait for it to be returned.");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        if (wallet.getDebt() > 0) {
            throw new DebtPendingException("You have a pending debt of $" + wallet.getDebt() +
                    ". Please clear it before renting another book.");
        }

        if(!book.isAvailable()){
            throw new BookAlreadyReservedException("Book is already reserved by Someone kindly rent some other book!!!");
        }

        RentalLimitRule rule = RentalRuleStrategy.getRule(user.getRole());
        if (!validateRentalLimit(user)) {
            return "Rental limit exceeded for your user type.";
        }

        if (days > rule.getMaxRentalDays()) {
            return "You cannot rent for more than " + rule.getMaxRentalDays() + " days.";
        }

        double cost = calculateRentalCost(book.getRentingPrice(), days, rule.getDiscount());
        if (wallet.getBalance() < cost) {
            return "Insufficient balance.";
        }

        wallet.setBalance(wallet.getBalance() - cost);
        walletRepo.save(wallet);


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
        return pricePerDay * days * (1 - discount);
    }


    @Override
    @Transactional
    public String extendRental(Long rentalId, int extraDays) throws BookNotReservedByUserException {
        Rental rental = rentalRepo.findById(rentalId).orElseThrow(() -> new RuntimeException("Rental not found"));
        Book book = bookRepo.findById(rental.getBook().getId()).orElseThrow(() -> new RuntimeException("Book not found"));
        Wallet wallet = walletRepo.findByUserId(rental.getUser().getId()).orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (!rental.getUser().getId().equals(rental.getUser().getId())) {
            throw new BookNotReservedByUserException("You have not rented this book.");
        }
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


    @Override
    @Transactional
    public String returnBook(Long rentalId) throws BookNotReservedByUserException {
        Rental rental = rentalRepo.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));

        if (rental.getIsReturned()) {
            return "Book is already returned.";
        }

        if (!rental.getUser().getId().equals(rental.getUser().getId())) {
            throw new BookNotReservedByUserException("You have not rented this book.");
        }

        User user = rental.getUser();
        Wallet wallet = walletRepo.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        Book book = rental.getBook();

        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = rental.getRentalEndDate();
        LocalDate startDate = rental.getRentalStartDate();

        RentalLimitRule rule = RentalRuleStrategy.getRule(user.getRole());
        double discount = rule.getDiscount();
        double pricePerDay = book.getRentingPrice();

        if (startDate.equals(endDate) && currentDate.isBefore(endDate)) {
            long unusedDays = 1; // At least 1 day charge
            double refund = unusedDays * pricePerDay * (1 - discount);
            wallet.setBalance(wallet.getBalance() + refund);
        } else if (currentDate.isBefore(endDate)) {
            long unusedDays = endDate.toEpochDay() - currentDate.toEpochDay();
            double refund = unusedDays * pricePerDay * (1 - discount);
            wallet.setBalance(wallet.getBalance() + refund);
        } else if (currentDate.isAfter(endDate)) {
            long overdueDays = currentDate.toEpochDay() - endDate.toEpochDay();
            double penalty = overdueDays * pricePerDay;
            wallet.setDebt(wallet.getDebt() + penalty);
        }

        rental.setIsReturned(true);
        rentalRepo.save(rental);
        walletRepo.save(wallet);

        return currentDate.isBefore(endDate) ?
                "Book returned early. Refund of $" + (pricePerDay * (1 - discount)) + " credited!" :
                "Book returned late. Penalty of $" + (pricePerDay) + " added to debt.";
    }

    @Override
    public List<RentalDto> getAllRentals() {
        List<Rental> rentals = rentalRepo.findAll();
        return rentals.stream()
                .map(this::convertTo)
                .collect(Collectors.toList());
    }

    private RentalDto convertTo(Rental rental) {
        RentalDto rentalDto = Converter.convertToDto(rental, RentalDto.class);
        rentalDto.setBookId(rental.getBook().getId());
        rentalDto.setUserId(rental.getUser().getId());
        return rentalDto;
    }

    private Rental convertToEntity(RentalDto rentalDto) {
        Rental rental = Converter.convertToEntity(rentalDto, Rental.class);
        Book book = bookRepo.findById(rentalDto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        User user = userRepo.findById(rentalDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        rental.setBook(book);
        rental.setUser(user);
        return  rental;
    }

    private boolean validateRentalLimit(User user) {
        RentalLimitRule rule = RentalRuleStrategy.getRule(user.getRole());
        long activeRentals = rentalRepo.countByUserIdAndIsReturned(user.getId(), false);
        return activeRentals < rule.getMaxRents(); // Active rental count validation
    }

    @Override
    @Transactional
    public String payDebt(Long userId, double amount) {
        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        double debt = wallet.getDebt();
        if (debt == 0) {
            return "You have no pending debt.";
        }

        if (amount >= debt) {
            wallet.setDebt(0.0);
            wallet.setBalance(wallet.getBalance() + (amount - debt));
        } else {
            wallet.setDebt(debt - amount);
        }

        walletRepo.save(wallet);
        return "Debt payment successful. Remaining debt: $" + wallet.getDebt();
    }



}
