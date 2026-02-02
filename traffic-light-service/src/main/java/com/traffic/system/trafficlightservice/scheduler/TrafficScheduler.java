package com.traffic.system.trafficlightservice.scheduler;

import com.traffic.system.trafficlightservice.entity.Intersection;
import com.traffic.system.trafficlightservice.enums.Direction;
import com.traffic.system.trafficlightservice.enums.LightColor;
import com.traffic.system.trafficlightservice.exception.TrafficLightServiceException;
import com.traffic.system.trafficlightservice.repository.IntersectionRepository;
import com.traffic.system.trafficlightservice.service.TrafficLightService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@XSlf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class TrafficScheduler {

    @NonNull
    private TrafficLightService trafficLightService;

    @NonNull
    private IntersectionRepository intersectionRepository;

    private String intersectionIdName;

    @Scheduled(fixedDelay = 35000)
    public void runTrafficCycle() throws TrafficLightServiceException {

        // This traffic runner would automate for an one intersection for now.,
        try {
            if (Objects.isNull(intersectionIdName)) {
                createIntersectionIdName();
                trafficLightService.createIntersection(intersectionIdName);
            }
            Intersection intersection = intersectionRepository.findByIdName(intersectionIdName).orElse(null);

            if (Objects.nonNull(intersection)) {
                trafficLightService.changeLight(intersection.getIdName(), Direction.NORTH, LightColor.GREEN);
                trafficLightService.changeLight(intersection.getIdName(), Direction.SOUTH, LightColor.GREEN);
                Thread.sleep(3000);
                trafficLightService.changeLight(intersection.getIdName(), Direction.NORTH, LightColor.YELLOW);
                trafficLightService.changeLight(intersection.getIdName(), Direction.SOUTH, LightColor.YELLOW);
                Thread.sleep(3000);
                trafficLightService.changeLight(intersection.getIdName(), Direction.NORTH, LightColor.RED);
                trafficLightService.changeLight(intersection.getIdName(), Direction.SOUTH, LightColor.RED);
                Thread.sleep(3000);
                trafficLightService.changeLight(intersection.getIdName(), Direction.EAST, LightColor.GREEN);
                trafficLightService.changeLight(intersection.getIdName(), Direction.WEST, LightColor.GREEN);
                Thread.sleep(3000);
                trafficLightService.changeLight(intersection.getIdName(), Direction.EAST, LightColor.YELLOW);
                trafficLightService.changeLight(intersection.getIdName(), Direction.WEST, LightColor.YELLOW);
                Thread.sleep(3000);
                trafficLightService.changeLight(intersection.getIdName(), Direction.EAST, LightColor.RED);
                trafficLightService.changeLight(intersection.getIdName(), Direction.WEST, LightColor.RED);
                Thread.sleep(3000);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TrafficLightServiceException(exception.getMessage(), exception);
        }
    }

    private void createIntersectionIdName() {
        intersectionIdName = "INTSEC-123-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
