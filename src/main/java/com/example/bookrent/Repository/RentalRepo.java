package com.example.bookrent.Repository;

import com.example.bookrent.Entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRepo extends JpaRepository<Rental,Long> {
    List<Rental> findByUserId(Long userId);
    long countByUserIdAndIsReturned(Long userId, Boolean isReturned);
    Optional<Rental> findByBookIdAndIsReturned(Long bookId, boolean isReturned);
}
