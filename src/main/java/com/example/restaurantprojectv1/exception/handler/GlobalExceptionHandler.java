package com.example.restaurantprojectv1.exception.handler;

import com.example.restaurantprojectv1.exception.*;
import com.example.restaurantprojectv1.exception.dto.ErrorResponse;
import com.example.restaurantprojectv1.exception.dto.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

    @ExceptionHandler(ExistedDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleExistedDataException(ExistedDataException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(OverReservationPersons.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleOverReservationPersons(OverReservationPersons ex){
        return ex.getMessage();
    }




}
