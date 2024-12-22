package com.example.bookrent.Controller;

import com.example.bookrent.Dto.BookDto;
import com.example.bookrent.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author) {
        List<BookDto> books = bookService.searchBooks(title, author);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDTO) {
        BookDto addedBook = bookService.addBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
    }

    @GetMapping("/popular")
    public List<BookDto> getPopularBooks() {
        return bookService.getPopularBooks();
    }

    @GetMapping("/highly-rated")
    public List<BookDto> getHighlyRatedBooks() {
        return bookService.getHighlyRatedBooks();
    }
}
