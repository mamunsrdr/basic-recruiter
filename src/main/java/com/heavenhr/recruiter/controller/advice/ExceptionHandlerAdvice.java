package com.heavenhr.recruiter.controller.advice;

import com.heavenhr.recruiter.app.error.ResourceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity handleException(ResourceException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", e.getHttpStatus().value());
        body.put("message", e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(body);
    }
}
