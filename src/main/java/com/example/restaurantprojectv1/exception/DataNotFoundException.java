package com.example.restaurantprojectv1.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String message){
        super(message);
        log.error(message);
    }
}
