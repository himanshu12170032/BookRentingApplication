package com.example.bookrent.Controller;

import com.example.bookrent.Dto.ReviewDto;
import com.example.bookrent.Entity.User;
import com.example.bookrent.Exception.UserNotFoundException;
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

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ReviewDto> submitReview(@RequestHeader("Authorization") String jwt,
                                                  @RequestParam Long bookId,
                                                  @RequestParam Double rating,
                                                  @RequestParam String reviewText) throws UserNotFoundException {
        // Find user by JWT
        User user = userService.findUserByProfile(jwt);

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
}
