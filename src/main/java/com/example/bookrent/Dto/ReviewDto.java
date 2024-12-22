package com.example.bookrent.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private Long userId;
    private Long bookId;
    private Double rating;
    private String reviewText;
}
