package com.example.bookrent.Repository;

import com.example.bookrent.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepo extends JpaRepository<Book,Long> {
    @Query("SELECT u FROM Book u WHERE " +
            "(:title IS NULL OR LOWER(u.title) LIKE LOWER(CONCAT('%', :title, '%'))) OR " +
            "(:author IS NULL OR LOWER(u.author) LIKE LOWER(CONCAT('%', :author, '%')))")
    List<Book> searchBooks(@Param("title") String title, @Param("author") String author);

}
