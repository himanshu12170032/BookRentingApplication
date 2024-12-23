package com.example.bookrent.Aspect;

import com.example.bookrent.Dto.BookDto;
import com.example.bookrent.Service.BookService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AverageRatingAspect {

    @Autowired
    private BookService bookService;

    @Before("execution(* com.example.bookrent.Service.BookServiceImpl.getBookById(..)) && args(id)")
    public void setAverageRating(Long id) throws Exception {
        BookDto bookDto = bookService.getBookDtoWithAverageRating(id);
        bookDto.setAverageRating(bookService.getAverageRatingForBook(id));
    }
}