package com.smoothstack.restaurantmicroservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestaurantTagNotFoundException extends RuntimeException {

    public RestaurantTagNotFoundException (String exception){
        super(exception);
    }

}
