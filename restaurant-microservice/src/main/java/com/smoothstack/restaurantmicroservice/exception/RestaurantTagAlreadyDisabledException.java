package com.smoothstack.restaurantmicroservice.exception;

public class RestaurantTagAlreadyDisabledException extends RuntimeException {

    private String message;

    public RestaurantTagAlreadyDisabledException(String message){
        super(message);
        this.message = message;
    }

    public RestaurantTagAlreadyDisabledException(){};
}
