package com.smoothstack.restaurantmicroservice.exception;

public class InvalidSearchException extends RuntimeException{

    private String message;

    public InvalidSearchException(String message){
        super(message);
        this.message = message;
    }

    public InvalidSearchException(){};
}
