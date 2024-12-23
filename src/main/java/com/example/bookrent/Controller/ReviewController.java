package com.example.bookrent.Controller;

import com.example.bookrent.Dto.ReviewDto;
import com.example.bookrent.Entity.User;
import com.example.bookrent.Exception.ResourceNotFoundException;
import com.example.bookrent.Exception.UserNotFoundException;
import com.example.bookrent.Helper.ReviewRequestDto;
import com.example.bookrent.Service.BookService;
import com.example.bookrent.Service.ReviewService;
import com.example.bookrent.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final BookService bookService;

    public ReviewController(ReviewService reviewService, UserService userService,BookService bookService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<ReviewDto> submitReview(@RequestHeader("Authorization") String jwt,
                                                  @RequestBody ReviewRequestDto reviewRequestDto
                                                  ) throws UserNotFoundException {

        User user = userService.findUserByProfile(jwt);
        Long bookId = reviewRequestDto.getBookId();
        Double rating = reviewRequestDto.getRating();
        String reviewText = reviewRequestDto.getReviewText();
        if (!bookService.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with ID: " + bookId);
        }
        ReviewDto reviewDto = reviewService.submitReview(user.getId(), bookId, rating, reviewText);
        return new ResponseEntity<>(reviewDto, HttpStatus.CREATED);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDto>> getReviewsForBook(@PathVariable Long bookId) {
        List<ReviewDto> reviews = reviewService.getReviewsForBook(bookId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/book/{bookId}/average")
    public ResponseEntity<Double> getAverageRatingForBook(@PathVariable Long bookId) {
        Double averageRating = reviewService.getAverageRatingForBook(bookId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getAllReviewsPostedByUser(@PathVariable Long userId) {
        List<ReviewDto> reviewDtos = reviewService.getAllReviewsByUser(userId);
        return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
    }
}
