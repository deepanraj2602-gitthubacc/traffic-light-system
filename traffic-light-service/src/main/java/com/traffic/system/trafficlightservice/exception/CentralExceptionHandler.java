
package com.traffic.system.trafficlightservice.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@XSlf4j
@Hidden
@RestControllerAdvice
@RequiredArgsConstructor
public class CentralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = TrafficLightServiceException.class)
    public ResponseEntity<ErrorResponse> handleTrafficLightServiceException(TrafficLightServiceException exception) {
        return returnResponse(exception.getMessage(), null, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return returnResponse("Unknown error, please contact administrator for support", null, INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> returnResponse(String exceptionMsg, Map<String, String> errors,
                                                         HttpStatusCode statusCode) {
        ErrorResponse commonErrResponse = !errors.isEmpty() ?
                new ErrorResponse(null, errors, now(), statusCode) :
                new ErrorResponse(exceptionMsg, null, now(), statusCode);
        return new ResponseEntity<>(commonErrResponse, statusCode);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    class ErrorResponse {
        private String message;
        private Map<String, String> errors;
        private LocalDateTime timestamp;
        private HttpStatusCode statusCode;
    }
}
