package com.example.bookrent.Controller;

import com.example.bookrent.Dto.RentalDto;
import com.example.bookrent.Dto.WalletDto;
import com.example.bookrent.Entity.User;
import com.example.bookrent.Exception.BookNotReservedByUserException;
import com.example.bookrent.Exception.UserNotFoundException;
import com.example.bookrent.Request.RentRequest;
import com.example.bookrent.Service.RentalService;
import com.example.bookrent.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;

    public RentalController(RentalService rentalService,UserService userService) {
        this.rentalService = rentalService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<RentalDto>> getRentals(@RequestHeader("Authorization") String jwt) throws UserNotFoundException {
        User user = userService.findUserByProfile(jwt);
        List<RentalDto> userRentals = rentalService.getAllRentals().stream()
                .filter(rentalDto -> rentalDto.getUserId().equals(user.getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userRentals);
    }

    @PostMapping("/rent")
    public ResponseEntity<String> rentBook(@RequestBody RentRequest rentRequest, @RequestHeader("Authorization") String jwt) throws UserNotFoundException {
        User user = userService.findUserByProfile(jwt);
        return ResponseEntity.ok(rentalService.rentBook(user.getId(), rentRequest.getBookId(), rentRequest.getDays()));
    }

    @PostMapping("/extend")
    public ResponseEntity<String> extendRental(@RequestParam Long rentalId, @RequestParam int extraDays, @RequestHeader("Authorization") String jwt) throws UserNotFoundException, BookNotReservedByUserException {
        User user = userService.findUserByProfile(jwt);
        boolean isUserRental = user.getRentals().stream().anyMatch(rental -> rental.getId().equals(rentalId));
        if (!isUserRental) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot extend a rental that is not yours.");
        }

        return ResponseEntity.ok(rentalService.extendRental(rentalId, extraDays));
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam Long rentalId,@RequestHeader("Authorization") String jwt) throws UserNotFoundException, BookNotReservedByUserException {
        User user = userService.findUserByProfile(jwt);
        boolean isUserRental = user.getRentals().stream().anyMatch(rental -> rental.getId().equals(rentalId));
        if (!isUserRental) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot return a rental that is not yours.");
        }
        return ResponseEntity.ok(rentalService.returnBook(rentalId));
    }
}

