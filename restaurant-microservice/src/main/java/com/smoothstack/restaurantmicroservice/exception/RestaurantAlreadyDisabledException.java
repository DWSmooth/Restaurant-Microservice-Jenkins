package com.smoothstack.restaurantmicroservice.exception;

public class RestaurantAlreadyDisabledException extends RuntimeException{
    private String message;

    public RestaurantAlreadyDisabledException(String message){
        super(message);
        this.message = message;
    }

    public RestaurantAlreadyDisabledException(){};
}
