package com.example.bookrent.Controller;

import com.example.bookrent.Dto.WalletDto;
import com.example.bookrent.Entity.User;
import com.example.bookrent.Exception.UserNotFoundException;
import com.example.bookrent.Service.UserService;
import com.example.bookrent.Service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;

    public WalletController(WalletService walletService,UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @PostMapping("/add-funds")
    public ResponseEntity<String> addFunds(
            @RequestParam Long userId,
            @RequestParam Double amount,
            @RequestHeader("Authorization") String jwt) throws UserNotFoundException {

        User user = userService.findUserByProfile(jwt);

        if (!user.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only add funds to your own account.");
        }

        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Invalid amount");
        }

        String result = walletService.addFunds(userId, amount);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/check-balance")
    public ResponseEntity<Double> checkMyBalance(@RequestHeader("Authorization") String jwt) throws UserNotFoundException {
        User user = userService.findUserByProfile(jwt);
        return ResponseEntity.ok(walletService.checkBalance(user.getId()));
    }
}
