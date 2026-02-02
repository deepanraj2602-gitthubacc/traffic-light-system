
package com.traffic.system.trafficlightservice.repository;

import com.traffic.system.trafficlightservice.config.BaseMySqlJpaTest;
import com.traffic.system.trafficlightservice.entity.LightHistory;
import com.traffic.system.trafficlightservice.enums.Direction;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.enums.LightColor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LightHistoryRepositoryTest extends BaseMySqlJpaTest {

    @Autowired
    private LightHistoryRepository lightHistoryRepository;

    private static final String INTERSECTION_TEST_NAME = "MAIN-ROAD-INTSEC-";
    private static final String ADMIN_USER = "Admin";

    @Test
    void saveLightHistory() {
        String idName = INTERSECTION_TEST_NAME + formatCurrentDateTime();

        LightHistory lightHistorySaved = lightHistoryRepository.save(createTestLightHistory(idName));

        assertNotNull(lightHistorySaved);
        assertNotNull(lightHistorySaved.getId());
        assertEquals(idName, lightHistorySaved.getIntersectionIdName());
    }

    @Test
    void saveAndFindLightHistoryPaginated() {
        String idName = INTERSECTION_TEST_NAME + formatCurrentDateTime();

        for (int index = 1; index <= 5; index++) {
            lightHistoryRepository.save(createTestLightHistory(idName));
        }

        Page<LightHistory> lightHistoryPage = lightHistoryRepository.findByIntersectionIdName(idName, PageRequest.of(0, 3));

        assertNotNull(lightHistoryPage);
        assertNotNull(lightHistoryPage.getContent());
        assertEquals(3, lightHistoryPage.getContent().size());
        assertEquals(5L, lightHistoryPage.getTotalElements());
    }

    private LightHistory createTestLightHistory(String idName) {
        LightHistory lightHistory = new LightHistory(Direction.NORTH, LightColor.GREEN, LocalDateTime.now(), idName, IntersectionAutoRunStatus.RUNNING);
        lightHistory.setCreatedBy(ADMIN_USER);
        lightHistory.setCreatedAt(LocalDateTime.now());
        lightHistory.setVersion(0);
        return lightHistory;
    }

    private String formatCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
