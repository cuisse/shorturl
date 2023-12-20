package io.github.cuisse.api.shorturl.exception.handler;

import io.github.cuisse.api.shorturl.exception.APIException;
import io.github.cuisse.api.shorturl.exception.ApiErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<Object> handleApiException(APIException exception) {
        return ResponseEntity.status(exception.getStatus()).body(
                new ApiErrorMessage(exception.getMessage())
        );
    }

}
