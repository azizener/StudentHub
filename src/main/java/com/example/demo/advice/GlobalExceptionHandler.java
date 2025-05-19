package com.example.demo.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

//wrap controller, (Creating global excpetion errors for only controllers)
@RestControllerAdvice
//for logs
@Slf4j
public class GlobalExceptionHandler {

    public static Map<String, Object> buildResponse(String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        response.put("message", message);
        return response;
    }
    
    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> handleConflictExceptions(Exception exception) {
        log.warn("Conflict occurred: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildResponse("Conflict", exception.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Validation error: {}", e.getMessage());
        Map<String, Object> response = buildResponse( "Validation Error", "Invalid input data");

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        response.put("errors", errors);
        return ResponseEntity.status(422).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("Constraint violation: {}", e.getMessage());
        Map<String, Object> response = buildResponse("Constraint Violation", "Invalid request parameters");

        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        response.put("errors", errors);
        return ResponseEntity.status(422).body(response);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, JsonParseException.class})
    public ResponseEntity<Map<String, Object>> handleJsonParsingError(Exception e) {
        log.warn("JSON parsing error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildResponse("Invalid Request", "Malformed or unreadable JSON"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleInternalServerErrorException(Exception exception) {
        log.error("Internal server error: {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse("Internal Server Error", exception.getMessage()));
    }
}