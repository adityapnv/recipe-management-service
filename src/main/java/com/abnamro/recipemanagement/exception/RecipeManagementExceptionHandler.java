package com.abnamro.recipemanagement.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class RecipeManagementExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {

        List<String> errorMessages = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        return getErrorResponseResponseEntity(HttpStatus.BAD_REQUEST,
                "Validation error(s) occurred. " + String.join(", ", errorMessages));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        return getErrorResponseResponseEntity(HttpStatus.BAD_REQUEST,
                String.join(", ", errorMessages));
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRecipeNotFoundException(RecipeNotFoundException ex) {
        return getErrorResponseResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(value = InvalidFormatException.class)
    public final ResponseEntity<ErrorResponse> httpMessageNotReadable(InvalidFormatException ex) {
        String failedPaths = ex.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining());
        return getErrorResponseResponseEntity(HttpStatus.BAD_REQUEST, String.format("Field %s has invalid value %s ", failedPaths, ex.getValue()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unHandledException(Exception ex){
        return getErrorResponseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred with error "+ex.getMessage());
    }

    private static ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(HttpStatus responseCode, String errorMessages) {
        ErrorResponse errorResponse = getErrorResponse(responseCode, errorMessages);
        return ResponseEntity.status(responseCode.value()).body(errorResponse);
    }

    private static ErrorResponse getErrorResponse(HttpStatus responseCode,String errorMessage) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(responseCode.value());
        errorResponse.setMessage(errorMessage);
        return errorResponse;
    }
}
