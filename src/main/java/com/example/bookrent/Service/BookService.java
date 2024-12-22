package com.example.bookrent.Service;

import com.example.bookrent.Dto.BookDto;
import com.example.bookrent.Exception.ResourceNotFoundException;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();
    BookDto addBook(BookDto bookDTO);
    BookDto getBookById(Long id) throws ResourceNotFoundException;
    public List<BookDto> searchBooks(String title, String genre);
    public List<BookDto> getPopularBooks();
    public List<BookDto> getHighlyRatedBooks();
}
