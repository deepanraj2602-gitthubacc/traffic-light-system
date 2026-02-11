package com.traffic.system.trafficlightservice.scheduler;

import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.exception.TrafficLightServiceException;
import com.traffic.system.trafficlightservice.service.TrafficLightService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;

@XSlf4j
@RequiredArgsConstructor
public class TrafficSignalTask implements Runnable {

    @NonNull
    private String intersectionId;
    @NonNull
    private IntersectionAutoRunStatus autoRunStatus;
    @NonNull
    private TrafficLightService trafficLightService;

    public void run() {
        try {
            log.info("{} run called", intersectionId);
            trafficLightService.changeLightToNextDirection(intersectionId);
        } catch (TrafficLightServiceException exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }
    }
}
