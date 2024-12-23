package com.example.bookrent.Controller;

import com.example.bookrent.Dto.WalletDto;
import com.example.bookrent.Entity.User;
import com.example.bookrent.Exception.UserNotFoundException;
import com.example.bookrent.Service.UserService;
import com.example.bookrent.Service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;

    public WalletController(WalletService walletService, UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @PostMapping("/create-wallet")
    public ResponseEntity<String> createWalletIfNotExists(
            @RequestParam Long userId,
            @RequestHeader("Authorization") String jwt) throws UserNotFoundException {
        User user = userService.findUserByProfile(jwt);

        if (!user.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only create a wallet for your own account.");
        }

        walletService.createWalletIfNotExists(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Wallet created successfully.");
    }

    @PostMapping("/add-funds")
    public ResponseEntity<String> addFunds(
            @RequestParam Double amount,
            @RequestHeader("Authorization") String jwt) throws UserNotFoundException {
        User user = userService.findUserByProfile(jwt);

        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Invalid amount.");
        }

        walletService.addFunds(user.getId(), amount);
        return ResponseEntity.ok("Funds added successfully.");
    }

    @GetMapping("/check-balance")
    public ResponseEntity<Object> checkMyBalance(@RequestHeader("Authorization") String jwt) throws UserNotFoundException {
        User user = userService.findUserByProfile(jwt);
        Double balance = walletService.checkBalance(user.getId());
        return ResponseEntity.ok(balance);
    }
}
