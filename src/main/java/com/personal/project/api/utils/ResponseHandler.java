package com.personal.project.api.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    private ResponseHandler() {}

    public static ResponseEntity<Object> responseBuilder(HttpStatus httpStatus, String message, Object responseObject){

        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", httpStatus.value());
        map.put("data", responseObject);

        return new ResponseEntity<>(map, httpStatus);

    }
}