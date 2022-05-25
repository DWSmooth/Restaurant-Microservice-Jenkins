package com.smoothstack.restaurantmicroservice.exception;

public class LocationNotFoundException extends RuntimeException {
    private String message;

    public LocationNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public LocationNotFoundException(){};
}
