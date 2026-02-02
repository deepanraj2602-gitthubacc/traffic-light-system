
package com.traffic.system.trafficlightservice.controller;

import com.traffic.system.trafficlightservice.dto.IntersectionDTO;
import com.traffic.system.trafficlightservice.dto.LightHistoryDTO;
import com.traffic.system.trafficlightservice.dto.LightHistoryPageDTO;
import com.traffic.system.trafficlightservice.dto.TrafficLightDTO;
import com.traffic.system.trafficlightservice.enums.Direction;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.enums.LightColor;
import com.traffic.system.trafficlightservice.service.TrafficLightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(TrafficLightController.class)
public class TrafficLightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrafficLightService trafficLightService;

    private static final String BASE_API_URL = "/v1/intersections/";
    private static final String INTERSECTION_TEST_NAME = "MAIN-ROAD-INTSEC-1";
    private static final String BASE_API_URL_WITH_INTERSECTION = BASE_API_URL + INTERSECTION_TEST_NAME;

    @Test
    void createIntersectionWithCreatedHttpStatus() throws Exception {
        when(trafficLightService.createIntersection(INTERSECTION_TEST_NAME)).thenReturn(buildIntersectionDTO());

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_API_URL_WITH_INTERSECTION))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idName").value(INTERSECTION_TEST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autoRunStatus").value(IntersectionAutoRunStatus.RUNNING.name()));

        verify(trafficLightService, times(1)).createIntersection(anyString());
    }

    @Test
    void changeLightWithOkHttpStatus() throws Exception {
        when(trafficLightService.changeLight(INTERSECTION_TEST_NAME, Direction.NORTH, LightColor.GREEN)).thenReturn(buildIntersectionDTO());

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_API_URL_WITH_INTERSECTION + "/changelight?direction=NORTH&color=GREEN"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idName").value(INTERSECTION_TEST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autoRunStatus").value(IntersectionAutoRunStatus.RUNNING.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trafficLights").isNotEmpty());

        verify(trafficLightService, times(1)).changeLight(anyString(), any(), any());
    }

    @Test
    void updateIntersectionPausedStateWithOkHttpStatus() throws Exception {
        when(trafficLightService.updateIntersectionState(INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.PAUSED))
                .thenReturn(new IntersectionDTO(INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.PAUSED.name()));

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_API_URL_WITH_INTERSECTION + "/pauselight"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idName").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idName").value(INTERSECTION_TEST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autoRunStatus").value(IntersectionAutoRunStatus.PAUSED.name()));

        verify(trafficLightService, times(1)).updateIntersectionState(anyString(), any());
    }

    @Test
    void updateIntersectionResumedStateWithOkHttpStatus() throws Exception {
        when(trafficLightService.updateIntersectionState(INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING))
                .thenReturn(buildIntersectionDTO());

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_API_URL_WITH_INTERSECTION + "/resumelight"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idName").value(INTERSECTION_TEST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autoRunStatus").value(IntersectionAutoRunStatus.RUNNING.name()));

        verify(trafficLightService, times(1)).updateIntersectionState(anyString(), any());
    }

    @Test
    void getIntersectionStateWithOkHttpStatus() throws Exception {
        when(trafficLightService.getIntersectionState(INTERSECTION_TEST_NAME)).thenReturn(buildIntersectionDTO());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_API_URL_WITH_INTERSECTION + "/lightstate"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idName").value(INTERSECTION_TEST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autoRunStatus").value(IntersectionAutoRunStatus.RUNNING.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trafficLights").isNotEmpty());

        verify(trafficLightService, times(1)).getIntersectionState(anyString());
    }

    @Test
    void getLightHistoryWithOkHttpStatus() throws Exception {
        when(trafficLightService.getLightHistory(INTERSECTION_TEST_NAME, 0, 5))
                .thenReturn(new LightHistoryPageDTO(0, 5, 4, 20L, buildLightHistoryDTO()));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_API_URL_WITH_INTERSECTION + "/lighthistory?pageNumber=0&pageSize=5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lightHistories").isNotEmpty());

        verify(trafficLightService, times(1)).getLightHistory(anyString(), anyInt(), anyInt());
    }

    private IntersectionDTO buildIntersectionDTO() {
        return IntersectionDTO.builder()
                .idName(INTERSECTION_TEST_NAME)
                .autoRunStatus(IntersectionAutoRunStatus.RUNNING.name())
                .trafficLights(buildTrafficLightDTO())
                .build();
    }

    private List<TrafficLightDTO> buildTrafficLightDTO() {
        return Arrays.asList(
                new TrafficLightDTO(Direction.NORTH.name(), LightColor.GREEN.name(), LocalDateTime.now()),
                new TrafficLightDTO(Direction.SOUTH.name(), LightColor.RED.name(), LocalDateTime.now()),
                new TrafficLightDTO(Direction.EAST.name(), LightColor.RED.name(), LocalDateTime.now()),
                new TrafficLightDTO(Direction.WEST.name(), LightColor.RED.name(), LocalDateTime.now()));
    }

    private List<LightHistoryDTO> buildLightHistoryDTO() {
        return List.of(
                new LightHistoryDTO(Direction.NORTH.name(), LightColor.GREEN.name(), LocalDateTime.now(), INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING.name()),
                new LightHistoryDTO(Direction.SOUTH.name(), LightColor.GREEN.name(), LocalDateTime.now(), INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING.name()),
                new LightHistoryDTO(Direction.EAST.name(), LightColor.RED.name(), LocalDateTime.now(), INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING.name()),
                new LightHistoryDTO(Direction.WEST.name(), LightColor.RED.name(), LocalDateTime.now(), INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING.name()),
                new LightHistoryDTO(Direction.SOUTH.name(), LightColor.YELLOW.name(), LocalDateTime.now(), INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.PAUSED.name()));
    }
}
