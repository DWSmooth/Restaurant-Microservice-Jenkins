package com.smoothstack.restaurantmicroservice.exception;

public class MenuItemNotFoundException extends RuntimeException{
    private String message;

    public MenuItemNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public MenuItemNotFoundException(){};
}