package com.example.bookrent.Helper;

import com.example.bookrent.Dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookRatingWithDto {
    private BookDto bookDto;
    private Double avgRating;
}
