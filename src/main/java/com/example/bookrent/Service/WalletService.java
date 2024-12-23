package com.example.bookrent.Service;

import com.example.bookrent.Dto.WalletDto;
import com.example.bookrent.Exception.UserNotFoundException;

import java.util.List;

public interface WalletService {
    WalletDto addFunds(Long userId, Double amount) throws UserNotFoundException;
    Double checkBalance(Long userId);
    List<WalletDto> getAllWallets();
    WalletDto createWalletIfNotExists(Long userId) throws UserNotFoundException;
}
