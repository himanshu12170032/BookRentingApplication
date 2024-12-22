package com.example.bookrent.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalDto {
    private Long id;
    private Long bookId;
    private Long userId;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
    private Boolean isReturned;
}
