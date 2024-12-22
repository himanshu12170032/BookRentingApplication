package com.example.bookrent.Controller;

import com.example.bookrent.Service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/add-funds")
    public ResponseEntity<String> addFunds(@RequestParam Long userId, @RequestParam Double amount) {
        return ResponseEntity.ok(walletService.addFunds(userId, amount));
    }

    @GetMapping("/check-balance")
    public ResponseEntity<Double> checkBalance(@RequestParam Long userId) {
        return ResponseEntity.ok(walletService.checkBalance(userId));
    }
}
