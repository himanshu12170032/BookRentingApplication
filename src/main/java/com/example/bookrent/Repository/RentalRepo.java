package com.example.bookrent.Repository;

import com.example.bookrent.Entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepo extends JpaRepository<Rental,Long> {
    List<Rental> findByUserId(Long userId);
}
