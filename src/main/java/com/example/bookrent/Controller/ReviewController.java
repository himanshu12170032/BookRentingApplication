package com.example.bookrent.Controller;

import com.example.bookrent.Dto.ReviewDto;
import com.example.bookrent.Service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ReviewDto submitReview(@RequestParam Long userId, @RequestParam Long bookId, @RequestParam Integer rating, @RequestParam String reviewText) {
        return reviewService.submitReview(userId, bookId, rating, reviewText);
    }

    @GetMapping("/book/{bookId}")
    public List<ReviewDto> getReviewsForBook(@PathVariable Long bookId) {
        return reviewService.getReviewsForBook(bookId);
    }

    @GetMapping("/book/{bookId}/average")
    public Double getAverageRatingForBook(@PathVariable Long bookId) {
        return reviewService.getAverageRatingForBook(bookId);
    }
}
