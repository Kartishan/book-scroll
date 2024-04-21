package com.kartishan.bookscroll.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> BookNotFoundExceptionHandler(BookNotFoundException ex){
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ErrorResponse> ImageNotFoundExceptionHandler(ImageNotFoundException ex){
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> CommentNotFoundExceptionHandler(CommentNotFoundException ex){
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ScrollNotFoundException.class)
    public ResponseEntity<ErrorResponse> ScrollNotFoundExceptionHandler(ScrollNotFoundException ex){
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
