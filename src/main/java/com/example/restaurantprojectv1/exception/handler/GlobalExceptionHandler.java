package com.example.restaurantprojectv1.exception.handler;

import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataNotFoundException(DataNotFoundException ex, HttpServletRequest httpServletRequest){
        return ErrorResponse.of(
                HttpStatus.BAD_REQUEST.toString(),
                ex.getMessage(),
                httpServletRequest.getRequestURI()
        );
    }
}
