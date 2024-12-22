package com.example.bookrent.Service;

import com.example.bookrent.Dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    public ReviewDto submitReview(Long userId, Long bookId, Integer rating, String reviewText);
    public List<ReviewDto> getReviewsForBook(Long bookId);
    public Double getAverageRatingForBook(Long bookId);
}
