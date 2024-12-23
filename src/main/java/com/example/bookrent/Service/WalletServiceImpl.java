package com.example.bookrent.Service;

import com.example.bookrent.Converter.Converter;
import com.example.bookrent.Dto.WalletDto;
import com.example.bookrent.Entity.User;
import com.example.bookrent.Entity.Wallet;
import com.example.bookrent.Exception.UserNotFoundException;
import com.example.bookrent.Exception.WalletAlreadyExistsException;
import com.example.bookrent.Repository.WalletRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepo walletRepo;
    private final UserService userService;

    public WalletServiceImpl(WalletRepo walletRepo, UserService userService) {
        this.walletRepo = walletRepo;
        this.userService = userService;
    }

    @Override
    public WalletDto addFunds(Long userId, Double amount) throws UserNotFoundException {
        Wallet wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepo.save(wallet);
        return convertToDto(wallet);
    }

    @Override
    public Double checkBalance(Long userId) {
        Wallet wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Wallet not found for user ID: " + userId));
        return wallet.getBalance();
    }

    @Override
    public List<WalletDto> getAllWallets() {
        List<Wallet> wallets = walletRepo.findAll();
        return wallets.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public WalletDto createWalletIfNotExists(Long userId) throws UserNotFoundException {
        Wallet existingWallet = walletRepo.findByUserId(userId).orElse(null);
        if (existingWallet != null) {
            throw new WalletAlreadyExistsException("Wallet already exists for user ID: " + userId);
        }
        User user = userService.findUserById(userId);
        Wallet newWallet = new Wallet();
        newWallet.setUser(user);
        newWallet.setBalance(0.0);
        walletRepo.save(newWallet);
        return convertToDto(newWallet);
    }

    private WalletDto convertToDto(Wallet wallet) {
        WalletDto walletDto = Converter.convertToDto(wallet, WalletDto.class);
        walletDto.setUserId(wallet.getUser().getId());
        return walletDto;
    }

    private Wallet convertToEntity(WalletDto walletDto) throws UserNotFoundException {
        Wallet wallet = Converter.convertToEntity(walletDto, Wallet.class);
        User user = userService.findUserById(walletDto.getUserId());
        wallet.setUser(user);
        return wallet;
    }
}
