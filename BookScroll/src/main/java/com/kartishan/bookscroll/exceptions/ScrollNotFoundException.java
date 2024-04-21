package com.kartishan.bookscroll.exceptions;

public class ScrollNotFoundException extends RuntimeException{
    public ScrollNotFoundException(String message){
        super(message);
    }
}
