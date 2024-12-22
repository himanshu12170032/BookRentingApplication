package com.example.bookrent.Service;

import com.example.bookrent.Converter.Converter;
import com.example.bookrent.Dto.BookDto;
import com.example.bookrent.Entity.Book;
import com.example.bookrent.Exception.ResourceNotFoundException;
import com.example.bookrent.Repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService{
    @Autowired
    BookRepo bookRepo;
    @Autowired
    private  ReviewService reviewService;

    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepo.findAll();
        List<BookDto> bookDtos = books.stream().map(this::convertToDTO).collect(Collectors.toList());
        return List.of();
    }
    @Override
    public BookDto addBook(BookDto bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book savedBook = bookRepo.save(book);
        return convertToDTO(savedBook);
    }

    @Override
    public BookDto getBookById(Long id) throws ResourceNotFoundException {
        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        return convertToDTO(book);
    }

    @Override
    public List<BookDto> searchBooks(String title, String genere) {
        List<Book> books = bookRepo.searchBooks(title, genere);
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDto convertToDTO(Book book){

//        return new BookDto(
//                book.getId(),
//                book.getTitle(),
//                book.getAuthor(),
//                book.getGenre(),
//                book.getRating(),
//                book.getRentingPrice(),
//                book.isAvailable()
//        );

        return Converter.convertToDto(book,BookDto.class);
    }

    private Book convertToEntity(BookDto bookDto) {
//        return Book.builder()
//                .title(bookDto.getTitle())
//                .author(bookDto.getAuthor())
//                .genre(bookDto.getGenre())
//                .rating(bookDto.getRating())
//                .rentingPrice(bookDto.getRentingPrice())
//                .isAvailable(bookDto.isAvailable())
//                .build();

        return Converter.convertToEntity(bookDto,Book.class);
    }

    public List<BookDto> getPopularBooks() {
        List<Book> books = bookRepo.findAll();
        return books.stream()
                .filter(book -> reviewService.getAverageRatingForBook(book.getId()) >= 4.0)  // Highlight books with average rating 4 or higher
                .sorted((b1, b2) -> Double.compare(reviewService.getAverageRatingForBook(b2.getId()), reviewService.getAverageRatingForBook(b1.getId())))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDto> getHighlyRatedBooks() {
        List<Book> books = bookRepo.findAll();
        return books.stream()
                .filter(book -> reviewService.getAverageRatingForBook(book.getId()) >= 4.5)  // Highlight books with average rating 4.5 or higher
                .sorted((b1, b2) -> Double.compare(reviewService.getAverageRatingForBook(b2.getId()), reviewService.getAverageRatingForBook(b1.getId())))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
