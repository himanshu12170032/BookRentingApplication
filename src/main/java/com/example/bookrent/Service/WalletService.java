package com.example.bookrent.Service;

import com.example.bookrent.Dto.WalletDto;

import java.util.List;

public interface WalletService {
    String addFunds(Long userId, Double amount);
    Double checkBalance(Long userId);
    List<WalletDto> getAllWallets();
}
