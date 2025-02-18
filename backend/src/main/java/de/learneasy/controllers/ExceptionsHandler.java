package de.learneasy.controllers;

import de.learneasy.controllers.exceptions.BadRequestException;
import de.learneasy.controllers.exceptions.ForbiddenException;
import de.learneasy.controllers.exceptions.InternalServerErrorException;
import de.learneasy.controllers.exceptions.NotFoundException;
import de.learneasy.datatransferobjects.ErrorDTO;
import de.learneasy.datatransferobjects.ErrorsDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler
{
    @ExceptionHandler(ForbiddenException.class)
    public @NotNull ResponseEntity<ErrorDTO> handleForbiddenExceptions(@NotNull ForbiddenException forbiddenException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO(forbiddenException.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public @NotNull ResponseEntity<ErrorDTO> handleNotFoundExceptions(@NotNull NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(notFoundException.getMessage()));
    }

    @ExceptionHandler({
        BadRequestException.class,
        ConstraintViolationException.class,
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class,
        HttpMessageNotReadableException.class,
        IllegalArgumentException.class
    })
    public @NotNull ResponseEntity<ErrorsDTO> handleBadRequestExceptions(@NotNull Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            switch(exception) {
                case BadRequestException badRequestException -> new ErrorsDTO(List.of(badRequestException.getMessage()));
                case ConstraintViolationException constraintViolationException -> new ErrorsDTO(constraintViolationException.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList());
                case MethodArgumentNotValidException methodArgumentNotValidException -> new ErrorsDTO(methodArgumentNotValidException.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()).toList());
                case MethodArgumentTypeMismatchException methodArgumentTypeMismatchException -> new ErrorsDTO(List.of("Value of parameter "+ methodArgumentTypeMismatchException.getName() +" has wrong type."));
                case MissingServletRequestParameterException missingServletRequestParameterException -> new ErrorsDTO(List.of("Value for parameter "+ missingServletRequestParameterException.getParameterName() +" is missing."));
                case HttpMessageNotReadableException ignored -> new ErrorsDTO(List.of("Request body is missing, has wrong format or has wrong value for attribute type."));
                case IllegalArgumentException illegalArgumentException -> new ErrorsDTO(List.of(illegalArgumentException.getMessage()));
                default -> throw new InternalServerErrorException("Unexpected error occurred.");
            }
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public @NotNull ResponseEntity<ErrorDTO> handleMethodNotSupportedExceptions(@NotNull HttpRequestMethodNotSupportedException ignored) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorDTO("Method not supported on this endpoint."));
    }

    @ExceptionHandler({
            InternalServerErrorException.class,
            IllegalStateException.class,
            Exception.class
    })
    public @NotNull ResponseEntity<ErrorDTO> handleOtherExceptions(@NotNull Exception exception)
    {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("Unexpected error occurred."));
    }
}
