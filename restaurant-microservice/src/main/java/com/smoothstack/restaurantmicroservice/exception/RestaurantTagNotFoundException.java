package com.smoothstack.restaurantmicroservice.exception;

public class RestaurantTagNotFoundException extends RuntimeException {
    private String message;

    public RestaurantTagNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public RestaurantTagNotFoundException(){};
}


