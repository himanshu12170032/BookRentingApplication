package com.example.bookrent.Controller;

import com.example.bookrent.Service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/rent")
    public ResponseEntity<String> rentBook(@RequestParam Long userId, @RequestParam Long bookId, @RequestParam int days) {
        return ResponseEntity.ok(rentalService.rentBook(userId, bookId, days));
    }

    @PostMapping("/extend")
    public ResponseEntity<String> extendRental(@RequestParam Long rentalId, @RequestParam int extraDays) {
        return ResponseEntity.ok(rentalService.extendRental(rentalId, extraDays));
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam Long rentalId) {
        return ResponseEntity.ok(rentalService.returnBook(rentalId));
    }
}

