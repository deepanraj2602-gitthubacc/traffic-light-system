
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
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@XSlf4j
@Hidden
@RestControllerAdvice
@RequiredArgsConstructor
public class CentralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = TrafficLightServiceException.class)
    public ResponseEntity<ErrorResponse> handleTrafficLightServiceException(TrafficLightServiceException exception) {
        log.error(exception.getMessage(), exception);
        return returnResponse(exception.getMessage(), null, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return returnResponse("Unknown error, please contact the administrator", null, INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> returnResponse(String exceptionMsg, Map<String, String> errors,
                                                         HttpStatusCode statusCode) {
        ErrorResponse commonErrResponse = (Objects.nonNull(errors) && !errors.isEmpty()) ?
                new ErrorResponse(null, errors, formatCurrentDateTime(), statusCode) :
                new ErrorResponse(exceptionMsg, null, formatCurrentDateTime(), statusCode);
        return new ResponseEntity<>(commonErrResponse, statusCode);
    }

    private String formatCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    class ErrorResponse {
        private String message;
        private Map<String, String> errors;
        private String timestamp;
        private HttpStatusCode statusCode;
    }
}
