package com.example.bookrent.Service;

import com.example.bookrent.Dto.ReviewDto;
import com.example.bookrent.Entity.Book;
import com.example.bookrent.Entity.Review;
import com.example.bookrent.Repository.BookRepo;
import com.example.bookrent.Repository.ReviewRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepo reviewRepo;
    private final BookRepo bookRepo;
    private final UserRepo userRepo;

    public ReviewServiceImpl(ReviewRepo reviewRepo, BookRepo bookRepo, UserRepo userRepo) {
        this.reviewRepo = reviewRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    public ReviewDto submitReview(Long userId, Long bookId, Integer rating, String reviewText) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(rating);
        review.setReviewText(reviewText);

        Review savedReview = reviewRepo.save(review);

        return new ReviewDto(savedReview.getId(), savedReview.getUser().getId(), savedReview.getBook().getId(), savedReview.getRating(), savedReview.getReviewText());
    }

    public List<ReviewDto> getReviewsForBook(Long bookId) {
        List<Review> reviews = reviewRepo.findByBookId(bookId);
        return reviews.stream().map(review -> new ReviewDto(review.getId(), review.getUser().getId(), review.getBook().getId(), review.getRating(), review.getReviewText())).collect(Collectors.toList());
    }

    public Double getAverageRatingForBook(Long bookId) {
        List<Review> reviews = reviewRepo.findByBookId(bookId);
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }
}
