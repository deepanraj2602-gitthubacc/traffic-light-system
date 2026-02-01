
package com.traffic.system.trafficlightservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrafficLightServiceException extends Exception {

    private Exception exception;

    public TrafficLightServiceException(String message) {
        super(message);
    }

    public TrafficLightServiceException(String message, Exception exception) {
        super(message, exception);
        this.exception = exception;
    }
}
