package com.example.bookrent.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private Double rating;
    private Double rentingPrice;
    private boolean isAvailable;


}

