package com.infosys.rewards.customerrewards.exception;

import com.infosys.rewards.customerrewards.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFoundException e, HttpServletRequest request) {

        log.error("Customer not found {}: ", e.getMessage());

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCustomerAlreadyExists(CustomerAlreadyExistsException e, HttpServletRequest request) {

        log.error("Customer already exist {}: ", e.getMessage());

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.error("Validation failed: {}", message);

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        log.warn("Constraint violation: {}", ex.getMessage());

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);

        return ResponseEntity.badRequest()
                .body(response);
    }
    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDateRangeException(InvalidDateRangeException ex,HttpServletRequest request) {

        log.warn("Invalid date range : {}", ex.getMessage());

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException ex,HttpServletRequest request) {

        log.warn("{}", ex.getMessage());

        ErrorResponse response = buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    private ErrorResponse buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {
        return ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
