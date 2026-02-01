
package com.traffic.system.trafficlightservice.service;


import com.traffic.system.trafficlightservice.dto.IntersectionDTO;
import com.traffic.system.trafficlightservice.dto.LightHistoryDTO;
import com.traffic.system.trafficlightservice.dto.LightHistoryPageDTO;
import com.traffic.system.trafficlightservice.dto.TrafficLightDTO;
import com.traffic.system.trafficlightservice.entity.Intersection;
import com.traffic.system.trafficlightservice.entity.LightHistory;
import com.traffic.system.trafficlightservice.entity.TrafficLight;
import com.traffic.system.trafficlightservice.enums.Direction;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.enums.LightColor;
import com.traffic.system.trafficlightservice.exception.TrafficLightServiceException;
import com.traffic.system.trafficlightservice.mapper.IntersectionMapper;
import com.traffic.system.trafficlightservice.repository.IntersectionRepository;
import com.traffic.system.trafficlightservice.repository.LightHistoryRepository;
import com.traffic.system.trafficlightservice.service.impl.TrafficLightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrafficLightServiceTest {

    @Mock
    private IntersectionRepository intersectionRepository;

    @Mock
    private LightHistoryRepository lightHistoryRepository;

    @Mock
    private IntersectionMapper intersectionMapper;

    @InjectMocks
    private TrafficLightServiceImpl trafficLightService;

    private Intersection intersection;

    private List<LightHistory> lightHistories;

    private static final String INTERSECTION_TEST_NAME = "MAIN-ROAD-INTSEC-1";
    private static final String INTERSECTION_TEST_NAME_2 = "MAIN-ROAD-INTSEC-2";
    private static final String INTERSECTION_ALREADY_EXISTS = "Intersection already exists";
    private static final String INTERSECTION_NOT_FOUND = "Intersection not found";
    private static final String CONFLICT_DIRECTION_NO_GREEN_ALLOWED = "Conflict direction, no green allowed";

    @BeforeEach
    void initialize() {
        List<TrafficLight> trafficLights = Arrays.asList(
                new TrafficLight(Direction.NORTH, LightColor.GREEN, LocalDateTime.now()),
                new TrafficLight(Direction.SOUTH, LightColor.RED, LocalDateTime.now()),
                new TrafficLight(Direction.EAST, LightColor.RED, LocalDateTime.now()),
                new TrafficLight(Direction.WEST, LightColor.RED, LocalDateTime.now()));

        intersection = new Intersection(INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING, trafficLights);

        lightHistories = List.of(
                new LightHistory(Direction.NORTH, LightColor.GREEN, LocalDateTime.now(), INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING),
                new LightHistory(Direction.SOUTH, LightColor.GREEN, LocalDateTime.now(), INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING),
                new LightHistory(Direction.EAST, LightColor.RED, LocalDateTime.now(), INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING),
                new LightHistory(Direction.WEST, LightColor.RED, LocalDateTime.now(), INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING),
                new LightHistory(Direction.SOUTH, LightColor.YELLOW, LocalDateTime.now(), INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.PAUSED));
    }

    @Test
    void createIntersectionSuccessful() throws TrafficLightServiceException {
        IntersectionDTO intersectionDto = new IntersectionDTO(INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING.name());
        when(intersectionRepository.existsByIdName(INTERSECTION_TEST_NAME)).thenReturn(false);
        when(intersectionRepository.save(any(Intersection.class))).thenReturn(intersection);
        when(intersectionMapper.toDto(any(Intersection.class))).thenReturn(intersectionDto);

        IntersectionDTO resultIntersectionDto = trafficLightService.createIntersection(INTERSECTION_TEST_NAME);

        // response assertion
        assertNotNull(resultIntersectionDto);
        assertEquals(INTERSECTION_TEST_NAME, resultIntersectionDto.getIdName());
        assertEquals(IntersectionAutoRunStatus.RUNNING.name(), resultIntersectionDto.getAutoRunStatus());

        // verify repository & mapper calls
        verify(intersectionRepository, times(1)).existsByIdName(INTERSECTION_TEST_NAME);
        verify(intersectionRepository, times(1)).save(any(Intersection.class));
        verify(intersectionMapper, times(1)).toDto(any(Intersection.class));
    }

    @Test
    void createIntersectionAlreadyExistsThrowingException() {
        when(intersectionRepository.existsByIdName(INTERSECTION_TEST_NAME)).thenReturn(true);

        TrafficLightServiceException exception = assertThrows(TrafficLightServiceException.class,
                () -> trafficLightService.createIntersection(INTERSECTION_TEST_NAME));

        assertEquals(INTERSECTION_ALREADY_EXISTS, exception.getMessage());

        verify(intersectionRepository, times(1)).existsByIdName(INTERSECTION_TEST_NAME);
        verifyNoInteractions(intersectionMapper);
    }

    @Test
    void changeLightSuccessful() throws TrafficLightServiceException {
        when(intersectionRepository.findByIdName(INTERSECTION_TEST_NAME)).thenReturn(Optional.of(intersection));
        setTrafficLight(Direction.SOUTH, LightColor.GREEN);
        when(intersectionRepository.save(any(Intersection.class))).thenReturn(intersection);
        when(intersectionMapper.toDtoWithTrafficLights(any(Intersection.class))).thenReturn(buildIntersectionDTO());

        IntersectionDTO resultIntersectionDto = trafficLightService.changeLight(INTERSECTION_TEST_NAME, Direction.SOUTH, LightColor.GREEN);

        assertNotNull(resultIntersectionDto);
        assertEquals(INTERSECTION_TEST_NAME, resultIntersectionDto.getIdName());
        assertEquals(LightColor.GREEN.name(), resultIntersectionDto.getTrafficLights().stream()
                .filter(light -> Direction.SOUTH.name().equals(light.getDirection()))
                .findFirst().orElse(new TrafficLightDTO()).getColor());

        verify(intersectionRepository, times(1)).findByIdName(INTERSECTION_TEST_NAME);
        verify(intersectionRepository, times(1)).save(any(Intersection.class));
        verify(intersectionMapper, times(1)).toDtoWithTrafficLights(any(Intersection.class));
        verify(lightHistoryRepository, times(1)).save(any(LightHistory.class));
    }

    @Test
    void changeLightToSameColorSuccessful() throws TrafficLightServiceException {
        when(intersectionRepository.findByIdName(INTERSECTION_TEST_NAME)).thenReturn(Optional.of(intersection));
        setTrafficLight(Direction.NORTH, LightColor.GREEN);
        when(intersectionRepository.save(any(Intersection.class))).thenReturn(intersection);
        when(intersectionMapper.toDtoWithTrafficLights(any(Intersection.class))).thenReturn(buildIntersectionDTO());

        IntersectionDTO resultIntersectionDto = trafficLightService.changeLight(INTERSECTION_TEST_NAME, Direction.NORTH, LightColor.GREEN);

        assertNotNull(resultIntersectionDto);
        assertEquals(INTERSECTION_TEST_NAME, resultIntersectionDto.getIdName());
        assertEquals(LightColor.GREEN.name(), resultIntersectionDto.getTrafficLights().stream()
                .filter(light -> Direction.NORTH.name().equals(light.getDirection()))
                .findFirst().orElse(new TrafficLightDTO()).getColor());

        verify(intersectionRepository, times(1)).findByIdName(INTERSECTION_TEST_NAME);
        verify(intersectionRepository, times(1)).save(any(Intersection.class));
        verify(intersectionMapper, times(1)).toDtoWithTrafficLights(any(Intersection.class));
        verify(lightHistoryRepository, times(1)).save(any(LightHistory.class));
    }

    @Test
    void changeLightToNonGreenSuccessfulEvenConflictExists() throws TrafficLightServiceException {
        when(intersectionRepository.findByIdName(INTERSECTION_TEST_NAME)).thenReturn(Optional.of(intersection));
        setTrafficLight(Direction.EAST, LightColor.GREEN);
        when(intersectionRepository.save(any(Intersection.class))).thenReturn(intersection);
        when(intersectionMapper.toDtoWithTrafficLights(any(Intersection.class))).thenReturn(buildIntersectionDTO());

        IntersectionDTO resultIntersectionDto = trafficLightService.changeLight(INTERSECTION_TEST_NAME, Direction.SOUTH, LightColor.YELLOW);

        assertNotNull(resultIntersectionDto);
        assertEquals(INTERSECTION_TEST_NAME, resultIntersectionDto.getIdName());
        assertEquals(IntersectionAutoRunStatus.RUNNING.name(), resultIntersectionDto.getAutoRunStatus());

        verify(intersectionRepository, times(1)).findByIdName(INTERSECTION_TEST_NAME);
        verify(intersectionRepository, times(1)).save(any(Intersection.class));
        verify(lightHistoryRepository, times(1)).save(any(LightHistory.class));
    }

    @Test
    void changeLightToGreenSuccessfulForTwoNoConflictDirections() throws TrafficLightServiceException {
        when(intersectionRepository.findByIdName(INTERSECTION_TEST_NAME)).thenReturn(Optional.of(intersection));
        setTrafficLight(Direction.NORTH, LightColor.GREEN);
        when(intersectionRepository.save(any(Intersection.class))).thenReturn(intersection);
        when(intersectionMapper.toDtoWithTrafficLights(any(Intersection.class))).thenReturn(buildIntersectionDTO());

        IntersectionDTO resultIntersectionDto = trafficLightService.changeLight(INTERSECTION_TEST_NAME, Direction.SOUTH, LightColor.GREEN);

        assertNotNull(resultIntersectionDto);
        assertEquals(INTERSECTION_TEST_NAME, resultIntersectionDto.getIdName());
        assertEquals(LightColor.GREEN.name(), resultIntersectionDto.getTrafficLights().stream()
                .filter(light -> Direction.NORTH.name().equals(light.getDirection()))
                .findFirst().orElse(new TrafficLightDTO()).getColor());
        assertEquals(LightColor.GREEN.name(), resultIntersectionDto.getTrafficLights().stream()
                .filter(light -> Direction.SOUTH.name().equals(light.getDirection()))
                .findFirst().orElse(new TrafficLightDTO()).getColor());

        verify(intersectionRepository, times(1)).findByIdName(INTERSECTION_TEST_NAME);
        verify(intersectionRepository, times(1)).save(any(Intersection.class));
        verify(intersectionMapper, times(1)).toDtoWithTrafficLights(any(Intersection.class));
        verify(lightHistoryRepository, times(1)).save(any(LightHistory.class));
    }

    @Test
    void changeLightSuccessfulToValidateLightHistoryData() throws TrafficLightServiceException {
        when(intersectionRepository.findByIdName(INTERSECTION_TEST_NAME)).thenReturn(Optional.of(intersection));
        setTrafficLight(Direction.SOUTH, LightColor.GREEN);
        when(intersectionRepository.save(any(Intersection.class))).thenReturn(intersection);
        when(intersectionMapper.toDtoWithTrafficLights(any(Intersection.class))).thenReturn(buildIntersectionDTO());

        ArgumentCaptor<LightHistory> lightHistoryArgCaptor = ArgumentCaptor.forClass(LightHistory.class);

        IntersectionDTO resultIntersectionDto = trafficLightService.changeLight(INTERSECTION_TEST_NAME, Direction.SOUTH, LightColor.GREEN);

        assertNotNull(resultIntersectionDto);
        assertEquals(INTERSECTION_TEST_NAME, resultIntersectionDto.getIdName());
        assertEquals(LightColor.GREEN.name(), resultIntersectionDto.getTrafficLights().stream()
                .filter(light -> Direction.SOUTH.name().equals(light.getDirection()))
                .findFirst().orElse(new TrafficLightDTO()).getColor());

        verify(intersectionRepository, times(1)).findByIdName(INTERSECTION_TEST_NAME);
        verify(intersectionRepository, times(1)).save(any(Intersection.class));
        verify(intersectionMapper, times(1)).toDtoWithTrafficLights(any(Intersection.class));
        verify(lightHistoryRepository).save(lightHistoryArgCaptor.capture());

        LightHistory lightHistory = lightHistoryArgCaptor.getValue();
        assertEquals(INTERSECTION_TEST_NAME, lightHistory.getIntersectionIdName());
        assertEquals(Direction.SOUTH, lightHistory.getDirection());
        assertEquals(LightColor.GREEN, lightHistory.getColor());
        assertNotNull(lightHistory.getChangedAt());
    }

    @Test
    void changeLightIntersectionNotFoundThrowingException() {
        when(intersectionRepository.findByIdName(INTERSECTION_TEST_NAME_2)).thenReturn(Optional.empty());

        TrafficLightServiceException exception = assertThrows(TrafficLightServiceException.class,
                () -> trafficLightService.changeLight(INTERSECTION_TEST_NAME_2, Direction.NORTH, LightColor.GREEN));

        assertEquals(INTERSECTION_NOT_FOUND, exception.getMessage());
        verifyNoInteractions(intersectionMapper, lightHistoryRepository);
    }

    @Test
    void changeLightConflictDirectionThrowingException() {
        when(intersectionRepository.findByIdName(INTERSECTION_TEST_NAME)).thenReturn(Optional.of(intersection));
        setTrafficLight(Direction.EAST, LightColor.GREEN);

        TrafficLightServiceException exception = assertThrows(TrafficLightServiceException.class,
                () -> trafficLightService.changeLight(INTERSECTION_TEST_NAME, Direction.EAST, LightColor.GREEN));

        assertEquals(CONFLICT_DIRECTION_NO_GREEN_ALLOWED, exception.getMessage());
        verifyNoInteractions(intersectionMapper, lightHistoryRepository);
    }

    @Test
    void updateIntersectionStateToPausedSuccessful() throws TrafficLightServiceException {
        IntersectionDTO intersectionDto = new IntersectionDTO(INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.PAUSED.name());
        when(intersectionRepository.findByIdName(INTERSECTION_TEST_NAME)).thenReturn(Optional.of(intersection));
        intersection.setAutoRunStatus(IntersectionAutoRunStatus.PAUSED);
        when(intersectionRepository.save(any(Intersection.class))).thenReturn(intersection);
        when(intersectionMapper.toDto(any(Intersection.class))).thenReturn(intersectionDto);

        IntersectionDTO resultIntersectionDto = trafficLightService.updateIntersectionState(INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.PAUSED);

        assertNotNull(resultIntersectionDto);
        assertEquals(INTERSECTION_TEST_NAME, resultIntersectionDto.getIdName());
        assertEquals(IntersectionAutoRunStatus.PAUSED.name(), resultIntersectionDto.getAutoRunStatus());

        verify(intersectionRepository, times(1)).findByIdName(INTERSECTION_TEST_NAME);
        verify(intersectionRepository, times(1)).save(any(Intersection.class));
        verify(intersectionMapper, times(1)).toDto(any(Intersection.class));
        verify(lightHistoryRepository, times(1)).save(any(LightHistory.class));
    }

    @Test
    void updateIntersectionStateToResumedSuccessful() throws TrafficLightServiceException {
        IntersectionDTO intersectionDto = new IntersectionDTO(INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.PAUSED.name());
        when(intersectionRepository.findByIdName(INTERSECTION_TEST_NAME)).thenReturn(Optional.of(intersection));
        intersection.setAutoRunStatus(IntersectionAutoRunStatus.RUNNING);
        when(intersectionRepository.save(any(Intersection.class))).thenReturn(intersection);
        when(intersectionMapper.toDto(any(Intersection.class))).thenReturn(intersectionDto);

        IntersectionDTO resultIntersectionDto = trafficLightService.updateIntersectionState(INTERSECTION_TEST_NAME, IntersectionAutoRunStatus.RUNNING);

        assertNotNull(resultIntersectionDto);
        assertEquals(INTERSECTION_TEST_NAME, resultIntersectionDto.getIdName());
        assertEquals(IntersectionAutoRunStatus.RUNNING.name(), resultIntersectionDto.getAutoRunStatus());

        verify(intersectionRepository, times(1)).findByIdName(INTERSECTION_TEST_NAME);
        verify(intersectionRepository, times(1)).save(any(Intersection.class));
        verify(intersectionMapper, times(1)).toDto(any(Intersection.class));
        verify(lightHistoryRepository, times(1)).save(any(LightHistory.class));
    }

    @Test
    void getIntersectionStateSuccessful() throws TrafficLightServiceException {
        when(intersectionRepository.findByIdName(INTERSECTION_TEST_NAME)).thenReturn(Optional.of(intersection));
        when(intersectionMapper.toDtoWithTrafficLights(any(Intersection.class))).thenReturn(buildIntersectionDTO());

        IntersectionDTO resultIntersectionDto = trafficLightService.getIntersectionState(INTERSECTION_TEST_NAME);

        assertNotNull(resultIntersectionDto);
        assertEquals(INTERSECTION_TEST_NAME, resultIntersectionDto.getIdName());
        assertFalse(resultIntersectionDto.getTrafficLights().isEmpty());
        assertTrue(resultIntersectionDto.getTrafficLights().stream()
                .allMatch(light -> java.util.Objects.nonNull(light.getLastChangedAt())));
        verify(intersectionRepository, times(1)).findByIdName(INTERSECTION_TEST_NAME);
        verify(intersectionMapper, times(1)).toDtoWithTrafficLights(any(Intersection.class));
    }

    @Test
    void getLightHistorySuccessful() throws TrafficLightServiceException {
        Pageable pageable = PageRequest.of(0, 5);
        Page<LightHistory> lightHistoryPage = new PageImpl<>(lightHistories, pageable, 10);
        when(lightHistoryRepository.findByIntersectionIdName(INTERSECTION_TEST_NAME, pageable))
                .thenReturn(lightHistoryPage);

        LightHistoryPageDTO resultLightHistoryPageDto = trafficLightService.getLightHistory(INTERSECTION_TEST_NAME, 0, 5);

        assertNotNull(resultLightHistoryPageDto);
        assertFalse(resultLightHistoryPageDto.getLightHistories().isEmpty());
        assertEquals(INTERSECTION_TEST_NAME, resultLightHistoryPageDto.getLightHistories().getFirst().getIntersectionIdName());
        verify(lightHistoryRepository, times(1)).findByIntersectionIdName(INTERSECTION_TEST_NAME, pageable);
    }

    private void setTrafficLight(Direction direction, LightColor color) {
        for (TrafficLight light : intersection.getTrafficLights()) {
            if (direction == light.getDirection()) {
                light.setColor(color);
                light.setLastChangedAt(LocalDateTime.now());
            }
        }
    }

    private IntersectionDTO buildIntersectionDTO() {
        return IntersectionDTO.builder()
                .idName(intersection.getIdName())
                .autoRunStatus(intersection.getAutoRunStatus().name())
                .trafficLights(buildTrafficLightDTO()).build();
    }

    private List<TrafficLightDTO> buildTrafficLightDTO() {
        List<TrafficLightDTO> trafficLightDtoList = new ArrayList<>();
        for (TrafficLight light : intersection.getTrafficLights()) {
            trafficLightDtoList.add(TrafficLightDTO.builder()
                    .direction(light.getDirection().name())
                    .color(light.getColor().name())
                    .lastChangedAt(light.getLastChangedAt()).build());
        }
        return trafficLightDtoList;
    }

    private LightHistoryPageDTO buildLightHistoryPageDTO(Page<LightHistory> lightHistoryPage) {
        return LightHistoryPageDTO.builder()
                .pageNumber(lightHistoryPage.getNumber())
                .pageSize(lightHistoryPage.getSize())
                .totalPages(lightHistoryPage.getTotalPages())
                .totalElements(lightHistoryPage.getTotalElements())
                .lightHistories(buildLightHistoryDTO(lightHistoryPage.getContent()))
                .build();
    }

    private List<LightHistoryDTO> buildLightHistoryDTO(List<LightHistory> lightHistories) {
        List<LightHistoryDTO> lightHistoryDtoList = new ArrayList<>();
        for (LightHistory lightHistory : lightHistories) {
            lightHistoryDtoList.add(LightHistoryDTO.builder()
                    .intersectionIdName(lightHistory.getIntersectionIdName())
                    .intersectionAutoRunStatus(lightHistory.getIntersectionAutoRunStatus().name())
                    .direction(lightHistory.getDirection().name())
                    .color(lightHistory.getColor().name())
                    .changedAt(lightHistory.getChangedAt())
                    .build());
        }
        return lightHistoryDtoList;
    }
}
