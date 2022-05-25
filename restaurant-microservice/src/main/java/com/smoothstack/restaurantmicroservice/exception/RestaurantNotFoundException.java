package com.smoothstack.restaurantmicroservice.exception;

public class RestaurantNotFoundException extends RuntimeException {
    private String message;

    public RestaurantNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public RestaurantNotFoundException(){};
}
