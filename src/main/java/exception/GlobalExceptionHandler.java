package exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import common.payload.ErrorDetails;
import exception.sub_exceptions.BadRequestException;
import exception.sub_exceptions.ResourceNotFoundException;

// This annotation makes this class a global exception handler for the entire application.
@ControllerAdvice
public class GlobalExceptionHandler {

        // Handles specific exceptions for when a resource is not found (404 Not Found).
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex,
                        WebRequest webRequest) {

                ErrorDetails errorDetails = ErrorDetails.builder().message(ex.getMessage())
                                .details(webRequest.getDescription(false).replace("uri=", "")).build();
                return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }

        // Handles specific exceptions for bad client request(400 Bad Request).
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorDetails> handleBadRequestException(ResourceNotFoundException ex,
                        WebRequest webRequest) {

                ErrorDetails errorDetails = ErrorDetails.builder().message(ex.getMessage())
                                .details(webRequest.getDescription(false).replace("uri=", "")).build();
                return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        // Handles validation errors from @Valid annotation (400 Bad Request).
        // This is one of the most common handlers you will need.
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                        WebRequest webRequest) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                ErrorDetails errorDetails = ErrorDetails.builder().message("Validation Failed")
                                .details(webRequest.getDescription(false).replace("uri=", "")).validationErrors(errors)
                                .build();
                return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        // parent exception method
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest webRequest) {
                ErrorDetails errorDetails = ErrorDetails.builder().message("An internal server error occured")
                                .details(webRequest.getDescription(false).replace("uri=", "")).build();
                return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }
}
