
package com.traffic.system.trafficlightservice.integration;

import com.traffic.system.trafficlightservice.entity.Intersection;
import com.traffic.system.trafficlightservice.entity.TrafficLight;
import com.traffic.system.trafficlightservice.enums.Direction;
import com.traffic.system.trafficlightservice.enums.LightColor;
import com.traffic.system.trafficlightservice.exception.TrafficLightServiceException;
import com.traffic.system.trafficlightservice.repository.IntersectionRepository;
import com.traffic.system.trafficlightservice.service.TrafficLightService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
public class TrafficLightServiceRepoIntegrationIT extends com.traffic.system.trafficlightservice.config.BaseIT {

    @Autowired
    private TrafficLightService trafficLightService;

    @Autowired
    private IntersectionRepository intersectionRepository;

    private static final String INTERSECTION_TEST_NAME = "MAIN-ROAD-INTSEC-";

    private String idName;

    @BeforeAll
    void initialize() {
        idName = INTERSECTION_TEST_NAME + formatCurrentDateTime();
    }

    @AfterAll
    void cleanup() {
        idName = null;
    }

    @Test
    void createIntersectionAndExists() throws TrafficLightServiceException {

        trafficLightService.createIntersection(idName);             // service call

        assertTrue(intersectionRepository.existsByIdName(idName));  // repository call
    }

    @Test
    void changeLightAndValidate() throws TrafficLightServiceException {

        trafficLightService.createIntersection(idName);
        trafficLightService.changeLight(idName, Direction.SOUTH, LightColor.GREEN);         // service call

        Optional<Intersection> intersectionDb = intersectionRepository.findByIdName(idName);  // repository call

        assertTrue(intersectionDb.isPresent());
        assertEquals(idName, intersectionDb.get().getIdName());
        assertEquals(LightColor.GREEN, intersectionDb.get().getTrafficLights().stream()
                .filter(light -> Direction.SOUTH == light.getDirection())
                .findFirst().orElse(new TrafficLight()).getColor());
    }

    private String formatCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
