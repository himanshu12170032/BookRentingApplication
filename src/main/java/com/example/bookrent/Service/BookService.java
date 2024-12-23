package com.example.bookrent.Service;

import com.example.bookrent.Dto.BookDto;
import com.example.bookrent.Exception.ResourceNotFoundException;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();
    BookDto addBook(BookDto bookDTO);
    BookDto getBookById(Long id) throws ResourceNotFoundException;
    public List<BookDto> searchBooks(String title, String genre);
    public List<BookDto> getPopularBooks(Double ratingThreshold);
    public List<BookDto> getHighlyRatedBooks();
    public Double getAverageRatingForBook(Long bookId);
    public boolean existsById(Long bookId);
    public BookDto getBookDtoWithAverageRating(Long bookId)  throws ResourceNotFoundException;
}
