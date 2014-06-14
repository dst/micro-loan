package com.stefanski.loan.rest.error;

import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.ex.RiskTooHighException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static com.stefanski.loan.rest.error.ErrorMessage.*;
import static java.util.stream.Collectors.toList;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());

        ErrorMessage error = new ErrorMessage(RESOURCE_NOT_FOUND_MSG);
        error.setDetails(ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RiskTooHighException.class)
    public ResponseEntity<ErrorMessage> handleRiskTooHighException(RiskTooHighException ex) {
        log.info("Loan forbidden because of too high risk: {}", ex.getMessage());

        ErrorMessage error = new ErrorMessage(RISK_TOO_HIGH_MSG);
        error.setDetails(ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        log.warn("Validation error: {}", result);

        ErrorMessage error = new ErrorMessage(INVALID_PARAMETERS_MSG);
        List<ParameterError> details = result.getFieldErrors()
                .stream()
                .map(ParameterError::fromFieldError)
                .collect(toList());
        error.setDetails(details);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorMessage> handleTypeMismatchException(TypeMismatchException ex) {
        log.warn("Invalid type of parameter: {}", ex.getMessage());

        ErrorMessage error = new ErrorMessage(INVALID_TYPE_MSG);
        // We have not sensible details

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleAnyException(Exception ex) {
        log.error("Not handled exception: {}", ex);

        ErrorMessage error = new ErrorMessage(INTERNAL_ERROR_MSG);
        // We have not sensible details

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}