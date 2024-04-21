package com.kartishan.bookscroll.exceptions;

public class ImageNotFoundException extends RuntimeException{
    public ImageNotFoundException(String message){
        super(message);
    }
}
