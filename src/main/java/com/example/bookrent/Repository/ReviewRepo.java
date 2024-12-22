package com.example.bookrent.Repository;

import com.example.bookrent.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review,Long> {
    @Query("SELECT r FROM Review r WHERE r.book.id = :bookId")
    List<Review> findByBookId(@Param("bookId") Long bookId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double findAverageRatingByBookId(@Param("bookId") Long bookId);

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.book.id = :bookId")
    List<Review> findByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
