package com.example.bookrent.Controller;
import com.example.bookrent.Entity.User;
import com.example.bookrent.Service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final BookService bookService;
    private final RentalService rentalService;
    private final WalletService walletService;
    private final NotificationService notificationService;
    private final UserService userService;

    public AdminController(BookService bookService, RentalService rentalService, WalletService walletService, NotificationService notificationService, UserService userService) {
        this.bookService = bookService;
        this.rentalService = rentalService;
        this.walletService = walletService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("Application is running");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}


