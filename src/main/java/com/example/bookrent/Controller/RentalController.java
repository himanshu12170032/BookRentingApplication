package com.example.bookrent.Controller;

import com.example.bookrent.Dto.RentalDto;
import com.example.bookrent.Dto.WalletDto;
import com.example.bookrent.Service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RentalDto> getRentals() {
        return rentalService.getAllRentals();
    }

    @PostMapping("/rent")
    public ResponseEntity<String> rentBook(@RequestParam Long userId, @RequestParam Long bookId, @RequestParam int days,@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(rentalService.rentBook(userId, bookId, days));
    }

    @PostMapping("/extend")
    public ResponseEntity<String> extendRental(@RequestParam Long rentalId, @RequestParam int extraDays, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(rentalService.extendRental(rentalId, extraDays));
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam Long rentalId,@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(rentalService.returnBook(rentalId));
    }
}

