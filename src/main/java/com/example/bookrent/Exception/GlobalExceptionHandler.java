package com.example.bookrent.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest req) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExistException(UserAlreadyExistsException ex, WebRequest req) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex,WebRequest req) {
        ErrorResponse errorResponse = new ErrorResponse("Internal server error", req.getDescription(false), LocalDateTime.now() );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
