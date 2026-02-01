
package com.traffic.system.trafficlightservice.integration;

import com.traffic.system.trafficlightservice.config.BaseIntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TestInstance(Lifecycle.PER_CLASS)
public class TrafficLightControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
    void createIntersection() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/intersections/" + idName))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idName").value(idName));
    }

    @Test
    void changeLight() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/intersections/" + idName + "/changelight"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idName").value(INTERSECTION_TEST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trafficLights").isNotEmpty());
    }

    private String formatCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
