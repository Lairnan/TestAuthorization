package com.lairnan.authorization.controllers;

import com.lairnan.authorization.NullPointerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public NullPointerResponse handleNullPointerException(NullPointerException npe) {
        NullPointerResponse response = new NullPointerResponse(npe.getMessage());
        return response;
    }
}
