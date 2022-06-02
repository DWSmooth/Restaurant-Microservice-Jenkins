package com.smoothstack.restaurantmicroservice.exception;

public class RestaurantTagAlreadyExistsException extends RuntimeException {
    private String message;
    public RestaurantTagAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
    public RestaurantTagAlreadyExistsException() {
    }
}