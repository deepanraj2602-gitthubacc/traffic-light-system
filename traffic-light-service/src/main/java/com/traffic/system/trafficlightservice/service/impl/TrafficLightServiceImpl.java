
package com.traffic.system.trafficlightservice.service.impl;


import com.traffic.system.trafficlightservice.dto.IntersectionDTO;
import com.traffic.system.trafficlightservice.dto.LightHistoryDTO;
import com.traffic.system.trafficlightservice.dto.LightHistoryPageDTO;
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
import com.traffic.system.trafficlightservice.service.TrafficLightService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@XSlf4j
@Service
@RequiredArgsConstructor
public class TrafficLightServiceImpl implements TrafficLightService {

    @NonNull
    private IntersectionRepository intersectionRepository;

    @NonNull
    private LightHistoryRepository lightHistoryRepository;

    @NonNull
    private IntersectionMapper intersectionMapper;

    private static final String ADMIN_USER = "Admin";

    private final Map<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    @Transactional
    public IntersectionDTO createIntersection(String idName) throws TrafficLightServiceException {
        IntersectionDTO intersectionDto = null;

        try {
            if (intersectionRepository.existsByIdName(idName)) {
                throw new TrafficLightServiceException("Intersection already exists");
            }
            Intersection intersection = buildIntersection(idName);
            setSystemAttributes(intersection);
            Intersection intersectionSaved = intersectionRepository.save(intersection);
            if (Objects.nonNull(intersectionSaved)) {
                intersectionDto = intersectionMapper.toDto(intersectionSaved);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TrafficLightServiceException(exception.getMessage(), exception);
        }
        return intersectionDto;
    }

    @Transactional
    public IntersectionDTO changeLight(String idName, Direction direction, LightColor color) throws TrafficLightServiceException {
        IntersectionDTO intersectionDto = null;

        ReentrantLock lock = locks.computeIfAbsent(idName, value -> new ReentrantLock());
        lock.lock();
        try {
            Intersection intersection = intersectionRepository.findByIdName(idName)
                    .orElseThrow(() -> new TrafficLightServiceException("Intersection not found"));

            if (intersection.getAutoRunStatus() == IntersectionAutoRunStatus.PAUSED) {
                throw new TrafficLightServiceException("Intersection paused");
            }

            checkConflict(intersection, direction, color);

            TrafficLight trafficLightUpdated = null;
            for (TrafficLight trafficLight : intersection.getTrafficLights()) {
                if (trafficLight.getDirection() == direction) {
                    trafficLight.setColor(color);
                    trafficLight.setLastChangedAt(LocalDateTime.now());
                    trafficLight.setUpdatedBy(ADMIN_USER);
                    trafficLight.setUpdatedAt(LocalDateTime.now());
                    trafficLightUpdated = trafficLight;
                }
            }

            intersection.setUpdatedBy(ADMIN_USER);
            intersection.setUpdatedAt(LocalDateTime.now());
            Intersection intersectionSaved = intersectionRepository.save(intersection);

            if (Objects.nonNull(intersectionSaved) && Objects.nonNull(trafficLightUpdated)) {
                saveLightHistory(intersectionSaved, trafficLightUpdated);
                intersectionDto = intersectionMapper.toDtoWithTrafficLights(intersectionSaved);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TrafficLightServiceException(exception.getMessage(), exception);
        } finally {
            lock.unlock();
        }
        return intersectionDto;
    }

    @Transactional
    public IntersectionDTO updateIntersectionState(String idName, IntersectionAutoRunStatus autoRunStatus) throws TrafficLightServiceException {
        IntersectionDTO intersectionDto = null;

        try {
            Intersection intersection = intersectionRepository.findByIdName(idName)
                    .orElseThrow(() -> new TrafficLightServiceException("Intersection not found"));

            if (autoRunStatus == IntersectionAutoRunStatus.PAUSED) {
                intersection.setAutoRunStatus(IntersectionAutoRunStatus.PAUSED);
            } else {
                intersection.setAutoRunStatus(IntersectionAutoRunStatus.RUNNING);
            }
            intersection.setUpdatedBy(ADMIN_USER);
            intersection.setUpdatedAt(LocalDateTime.now());
            Intersection intersectionSaved = intersectionRepository.save(intersection);

            if (Objects.nonNull(intersectionSaved)) {
                saveLightHistory(intersectionSaved, new TrafficLight(null, null, null));
                intersectionDto = intersectionMapper.toDto(intersectionSaved);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TrafficLightServiceException(exception.getMessage(), exception);
        }
        return intersectionDto;
    }

    @Transactional(readOnly = true)
    public IntersectionDTO getIntersectionState(String idName) throws TrafficLightServiceException {
        IntersectionDTO intersectionDto = null;

        try {
            Intersection intersection = intersectionRepository.findByIdName(idName)
                    .orElseThrow(() -> new TrafficLightServiceException("Intersection not found"));

            if (Objects.nonNull(intersection)) {
                intersectionDto = intersectionMapper.toDtoWithTrafficLights(intersection);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TrafficLightServiceException(exception.getMessage(), exception);
        }
        return intersectionDto;
    }

    @Transactional(readOnly = true)
    public LightHistoryPageDTO getLightHistory(String idName, Integer pageNumber, Integer pageSize) throws TrafficLightServiceException {
        LightHistoryPageDTO lightHistoryPageDto = null;

        try {
            Page<LightHistory> lightHistoryPage = lightHistoryRepository.findByIntersectionIdName(idName, PageRequest.of(pageNumber, pageSize));

            if (Objects.nonNull(lightHistoryPage)) {
                List<LightHistoryDTO> lightHistories = new ArrayList<>();
                for (LightHistory lightHistory : lightHistoryPage) {
                    lightHistories.add(LightHistoryDTO.builder().intersectionIdName(lightHistory.getIntersectionIdName())
                            .intersectionAutoRunStatus(lightHistory.getIntersectionAutoRunStatus().name())
                            .direction(Objects.nonNull(lightHistory.getDirection()) ? lightHistory.getDirection().name() : null)
                            .color(Objects.nonNull(lightHistory.getColor()) ? lightHistory.getColor().name() : null)
                            .changedAt(Objects.nonNull(lightHistory.getChangedAt()) ? lightHistory.getChangedAt() : null).build());
                }
                lightHistoryPageDto = LightHistoryPageDTO.builder().pageNumber(lightHistoryPage.getNumber())
                        .pageSize(lightHistoryPage.getContent().size())
                        .totalPages(lightHistoryPage.getTotalPages())
                        .totalElements(lightHistoryPage.getTotalElements())
                        .lightHistories(lightHistories).build();
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TrafficLightServiceException(exception.getMessage(), exception);
        }
        return lightHistoryPageDto;
    }

    private void saveLightHistory(Intersection intersection, TrafficLight trafficLight) {
        LightHistory lightHistory = LightHistory.builder().intersectionIdName(intersection.getIdName())
                .intersectionAutoRunStatus(intersection.getAutoRunStatus())
                .direction(trafficLight.getDirection())
                .color(trafficLight.getColor())
                .changedAt(trafficLight.getLastChangedAt()).build();
        setSystemAttributes(lightHistory);
        lightHistoryRepository.save(lightHistory);
    }

    private void checkConflict(Intersection intersection, Direction direction, LightColor color) throws TrafficLightServiceException {
        if (color == LightColor.GREEN)
            for (TrafficLight trafficLight : intersection.getTrafficLights()) {
                if (isConflictFound(direction, trafficLight.getDirection())
                        && trafficLight.getColor() == LightColor.GREEN) {
                    throw new TrafficLightServiceException("Conflict direction, no green allowed");
                }
            }
    }

    private boolean isConflictFound(Direction dir1, Direction dir2) {
        return (isNorthOrSouth(dir1) && isEastOrWest(dir2)) || (isEastOrWest(dir1) && isNorthOrSouth(dir2));
    }

    private boolean isNorthOrSouth(Direction direction) {
        return Direction.NORTH == direction || Direction.SOUTH == direction;
    }

    private boolean isEastOrWest(Direction direction) {
        return Direction.EAST == direction || Direction.WEST == direction;
    }

    private Intersection buildIntersection(String idName) {
        Intersection intersection = Intersection.builder().idName(idName)
                .autoRunStatus(IntersectionAutoRunStatus.RUNNING).build();

        List<TrafficLight> trafficLights = Arrays.asList(
                new TrafficLight(Direction.NORTH, LightColor.RED, LocalDateTime.now()),
                new TrafficLight(Direction.SOUTH, LightColor.RED, LocalDateTime.now()),
                new TrafficLight(Direction.EAST, LightColor.RED, LocalDateTime.now()),
                new TrafficLight(Direction.WEST, LightColor.RED, LocalDateTime.now()));

        for (TrafficLight trafficLight : trafficLights) {
            setSystemAttributes(trafficLight);
            trafficLight.setIntersection(intersection);
        }
        intersection.setTrafficLights(trafficLights);

        return intersection;
    }

    private void setSystemAttributes(Intersection intersection) {
        intersection.setCreatedBy(ADMIN_USER);
        intersection.setCreatedAt(LocalDateTime.now());
        intersection.setVersion(0);
    }

    private void setSystemAttributes(TrafficLight trafficLight) {
        trafficLight.setCreatedBy(ADMIN_USER);
        trafficLight.setCreatedAt(LocalDateTime.now());
        trafficLight.setVersion(0);
    }

    private void setSystemAttributes(LightHistory lightHistory) {
        lightHistory.setCreatedBy(ADMIN_USER);
        lightHistory.setCreatedAt(LocalDateTime.now());
        lightHistory.setVersion(0);
    }
}
