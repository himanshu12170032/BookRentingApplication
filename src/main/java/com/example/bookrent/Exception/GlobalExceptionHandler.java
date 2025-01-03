package com.example.bookrent.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest req) {
        // Customize the response for access denied errors
        ErrorResponse errorResponse = new ErrorResponse("Access Denied", req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);  // You can also use HttpStatus.UNAUTHORIZED if needed
    }
    @ExceptionHandler(WalletAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleWalletAlreadyExists(WalletAlreadyExistsException e, WebRequest req) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), req.getDescription(false), LocalDateTime.now());
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookAlreadyReservedException.class)
    public ResponseEntity<ErrorResponse> handleBookAlreadyReserved(BookAlreadyReservedException e, WebRequest req) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), req.getDescription(false), LocalDateTime.now());
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DebtPendingException.class)
    public ResponseEntity<ErrorResponse> handleDebtPending(DebtPendingException e, WebRequest req) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), req.getDescription(false), LocalDateTime.now());
        return  new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookNotReservedByUserException.class)
    public ResponseEntity<ErrorResponse> handleBookNotReservedByUser(BookNotReservedByUserException e, WebRequest req) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), req.getDescription(false), LocalDateTime.now());
        return  new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex,WebRequest req) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), req.getDescription(false), LocalDateTime.now() );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
