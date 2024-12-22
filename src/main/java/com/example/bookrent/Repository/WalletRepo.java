package com.example.bookrent.Repository;

import com.example.bookrent.Entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepo extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findByUserId(Long userId);
}
