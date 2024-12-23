package com.example.bookrent.Service;

import com.example.bookrent.Converter.Converter;
import com.example.bookrent.Dto.BookDto;
import com.example.bookrent.Entity.Book;
import com.example.bookrent.Entity.Review;
import com.example.bookrent.Exception.ResourceNotFoundException;
import com.example.bookrent.Helper.BookRatingWithDto;
import com.example.bookrent.Repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private ReviewService reviewService;

    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepo.findAll();
        return books.stream()
                .map(book -> getBookDtoWithAverageRating(book.getId()))  // Use the new method here
                .collect(Collectors.toList());
    }

    @Override
    public BookDto addBook(BookDto bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book savedBook = bookRepo.save(book);
        return convertToDTO(savedBook);
    }

    @Override
    public BookDto getBookById(Long id) throws ResourceNotFoundException {
        return getBookDtoWithAverageRating(id);  // Use the new method to get the book DTO with average rating
    }

    @Override
    public List<BookDto> searchBooks(String title, String genre) {
        List<Book> books = bookRepo.searchBooks(title, genre);
        return books.stream()
                .map(book -> getBookDtoWithAverageRating(book.getId()))  // Use the new method here
                .collect(Collectors.toList());
    }

    public BookDto getBookDtoWithAverageRating(Long bookId) throws ResourceNotFoundException {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));

        Double avgRating = reviewService.getAverageRatingForBook(bookId);
        BookDto bookDto = convertToDTO(book);
        bookDto.setAverageRating(avgRating);
        return bookDto;
    }

    @Override
    public Double getAverageRatingForBook(Long bookId) {
        return reviewService.getAverageRatingForBook(bookId);
    }

    @Override
    public boolean existsById(Long bookId) {
        return bookRepo.existsById(bookId);
    }

    @Override
    public List<BookDto> getPopularBooks(Double ratingThreshold) {
        List<Book> books = bookRepo.findAll();

        return books.stream()
                .map(book -> getBookDtoWithAverageRating(book.getId()))  // Use the new method here
                .filter(bookDto -> bookDto.getAverageRating() >= ratingThreshold)
                .sorted((b1, b2) -> Double.compare(b2.getAverageRating(), b1.getAverageRating()))
                .collect(Collectors.toList());
    }

    public List<BookDto> getHighlyRatedBooks() {
        List<Book> books = bookRepo.findAll();
        Double ratingThreshold = 4.5;
        return books.stream()
                .map(book -> getBookDtoWithAverageRating(book.getId()))  // Use the new method here
                .filter(bookDto -> bookDto.getAverageRating() >= ratingThreshold)
                .sorted((b1, b2) -> Double.compare(b2.getAverageRating(), b1.getAverageRating()))
                .collect(Collectors.toList());
    }

    public BookDto convertToDTO(Book book) {
        return Converter.convertToDto(book, BookDto.class);
    }
    private Book convertToEntity(BookDto bookDto) {
        return Converter.convertToEntity(bookDto, Book.class);
    }
}
