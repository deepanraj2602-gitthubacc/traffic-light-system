
package com.traffic.system.trafficlightservice.repository;

import com.traffic.system.trafficlightservice.config.BaseMySqlJpaTest;
import com.traffic.system.trafficlightservice.entity.Intersection;
import com.traffic.system.trafficlightservice.entity.TrafficLight;
import com.traffic.system.trafficlightservice.enums.Direction;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.enums.LightColor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntersectionRepositoryTest extends BaseMySqlJpaTest {

    @Autowired
    private IntersectionRepository intersectionRepository;

    private static final String INTERSECTION_TEST_NAME = "MAIN-ROAD-INTSEC-";

    @Test
    void saveIntersection() {
        String idName = INTERSECTION_TEST_NAME + formatCurrentDateTime();

        Intersection intersectionSaved = intersectionRepository.save(createTestIntersection(idName));

        assertNotNull(intersectionSaved);
        assertNotNull(intersectionSaved.getId());
        assertEquals(idName, intersectionSaved.getIdName());
        assertFalse(intersectionSaved.getTrafficLights().isEmpty());
    }

    @Test
    void saveAndExistsIntersection() {
        String idName = INTERSECTION_TEST_NAME + formatCurrentDateTime();

        intersectionRepository.save(createTestIntersection(idName));

        Boolean intersectionExists = intersectionRepository.existsByIdName(idName);

        assertTrue(intersectionExists);
    }

    @Test
    void saveAndFindIntersection() {
        String idName = INTERSECTION_TEST_NAME + formatCurrentDateTime();

        intersectionRepository.save(createTestIntersection(idName));

        Optional<Intersection> intersectionDb = intersectionRepository.findByIdName(idName);

        assertTrue(intersectionDb.isPresent());
        assertEquals(idName, intersectionDb.get().getIdName());
        assertEquals(IntersectionAutoRunStatus.RUNNING, intersectionDb.get().getAutoRunStatus());
        assertFalse(intersectionDb.get().getTrafficLights().isEmpty());
    }

    private Intersection createTestIntersection(String idName) {
        List<TrafficLight> trafficLights = Arrays.asList(
                new TrafficLight(Direction.NORTH, LightColor.GREEN, LocalDateTime.now()),
                new TrafficLight(Direction.SOUTH, LightColor.RED, LocalDateTime.now()),
                new TrafficLight(Direction.EAST, LightColor.RED, LocalDateTime.now()),
                new TrafficLight(Direction.WEST, LightColor.RED, LocalDateTime.now()));

        return new Intersection(idName, IntersectionAutoRunStatus.RUNNING, trafficLights);
    }

    private String formatCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
