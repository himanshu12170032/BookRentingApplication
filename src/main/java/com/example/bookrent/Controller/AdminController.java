package com.example.bookrent.Controller;
import com.example.bookrent.Dto.BookDto;
import com.example.bookrent.Dto.RentalDto;
import com.example.bookrent.Dto.WalletDto;
import com.example.bookrent.Entity.Book;
import com.example.bookrent.Entity.Rental;
import com.example.bookrent.Entity.Wallet;
import com.example.bookrent.Service.BookService;
import com.example.bookrent.Service.NotificationService;
import com.example.bookrent.Service.RentalService;
import com.example.bookrent.Service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final BookService bookService;
    private final RentalService rentalService;
    private final WalletService walletService;
    private final NotificationService notificationService;

    public AdminController(BookService bookService, RentalService rentalService, WalletService walletService, NotificationService notificationService) {
        this.bookService = bookService;
        this.rentalService = rentalService;
        this.walletService = walletService;
        this.notificationService = notificationService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("Application is running");
    }

    @GetMapping("/notifications/send")
    public ResponseEntity<String> sendNotifications() {
        notificationService.sendNotifications();
        return ResponseEntity.ok("Notifications processed");
    }

    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/rentals")
    public List<RentalDto> getRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/wallets")
    public List<WalletDto> getWallets() {
        return walletService.getAllWallets();
    }
}


