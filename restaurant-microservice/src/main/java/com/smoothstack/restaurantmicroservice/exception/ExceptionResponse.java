package com.smoothstack.restaurantmicroservice.exception;

import java.util.Date;

public class ExceptionResponse {

    private Date timestamp;
    private String message;
    private String details;

    public ExceptionResponse(Date timestamp, String message, String detils){
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = detils;
    }

    public Date getTimestamp(){
        return timestamp;
    }

    public String getMessage(){
        return message;
    }

    public String getDetails(){
        return details;
    }

}
