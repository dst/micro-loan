package com.stefanski.loan.rest.error;

import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.ex.RiskTooHighException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.stefanski.loan.rest.error.ErrorMessage.*;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.*;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        ErrorMessage error = new ErrorMessage(RESOURCE_NOT_FOUND_ERR, ex.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(error, NOT_FOUND);
    }

    @ExceptionHandler(RiskTooHighException.class)
    public ResponseEntity<ErrorMessage> handleRiskTooHighException(RiskTooHighException ex) {
        log.info("Loan forbidden because of too high risk: {}", ex.getMessage());
        ErrorMessage error = new ErrorMessage(RISK_TOO_HIGH_ERR, ex.getMessage(), FORBIDDEN);
        return new ResponseEntity<>(error, FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        log.warn("Validation error: {}", result);

        String message = result.getFieldErrors()
                .stream()
                .map(ParameterError::fromFieldError)
                .map(ParameterError::getDescription)
                .collect(joining(";"));

        ErrorMessage error = new ErrorMessage(INVALID_PARAM_ERR, message, BAD_REQUEST);
        return new ResponseEntity<>(error, BAD_REQUEST);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorMessage> handleTypeMismatchException(TypeMismatchException ex) {
        log.warn("Invalid type of parameter: {}", ex.getMessage());
        ErrorMessage error = new ErrorMessage(INVALID_TYPE_ERR, INVALID_TYPE_ERR, BAD_REQUEST);
        return new ResponseEntity<>(error, BAD_REQUEST);
    }
}
