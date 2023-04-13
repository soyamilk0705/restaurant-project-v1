package com.example.restaurantprojectv1.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExistedDataException extends RuntimeException{

    public ExistedDataException(String message){
        super(message);
        log.error(message);
    }
}
