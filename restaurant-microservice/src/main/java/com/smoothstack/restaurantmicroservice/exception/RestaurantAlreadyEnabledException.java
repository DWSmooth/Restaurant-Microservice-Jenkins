package com.smoothstack.restaurantmicroservice.exception;

public class RestaurantAlreadyEnabledException extends RuntimeException {

    private String message;

    public RestaurantAlreadyEnabledException(String message){
        super(message);
        this.message = message;
    }

    public RestaurantAlreadyEnabledException(){};
}
