package com.example.bookrent.Service;

import com.example.bookrent.Converter.Converter;
import com.example.bookrent.Dto.WalletDto;
import com.example.bookrent.Entity.Wallet;
import com.example.bookrent.Repository.WalletRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService{
    private final WalletRepo walletRepo;

    public WalletServiceImpl(WalletRepo walletRepo) {
        this.walletRepo = walletRepo;
    }

    @Override
    public String addFunds(Long userId, Double amount) {
        Wallet wallet = walletRepo.findByUserId(userId).orElseGet(() -> {
            Wallet newWallet = new Wallet();
            newWallet.setUserId(userId);
            newWallet.setBalance(0.0);
            return walletRepo.save(newWallet);
        });
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepo.save(wallet);
        return "Funds added successfully";
    }

    @Override
    public Double checkBalance(Long userId) {
        Wallet wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Wallet not found"));
        return wallet.getBalance();
    }

    @Override
    public List<WalletDto> getAllWallets() {
        List<Wallet> wallets = walletRepo.findAll();
        List<WalletDto> walletDtos = wallets.stream().map(this::convertToDto).collect(Collectors.toList());
        return walletDtos;
    }

    private WalletDto convertToDto(Wallet wallet){
//        return new WalletDto(wallet.getId(),wallet.getUserId(),wallet.getBalance());
        return Converter.convertToDto(wallet,WalletDto.class);
    }
    private Wallet convertToEntity(WalletDto walletDto){
        return Converter.convertToEntity(walletDto,Wallet.class);
    }
}
