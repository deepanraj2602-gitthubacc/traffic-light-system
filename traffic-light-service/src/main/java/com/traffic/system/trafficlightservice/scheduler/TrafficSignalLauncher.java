package com.traffic.system.trafficlightservice.scheduler;

import com.traffic.system.trafficlightservice.entity.Intersection;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.exception.TrafficLightServiceException;
import com.traffic.system.trafficlightservice.repository.IntersectionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@XSlf4j
@Component
@RequiredArgsConstructor
public class TrafficSignalLauncher implements ApplicationRunner {

    @NonNull
    private IntersectionRepository intersectionRepository;

    @NonNull
    private TrafficSignalTaskManager trafficSignalTaskManager;

    public void run(ApplicationArguments args) throws TrafficLightServiceException {
        try {

            List<Intersection> runningIntersections = intersectionRepository
                    .findByAutoRunStatus(IntersectionAutoRunStatus.RUNNING);

            if (Objects.nonNull(runningIntersections) && !runningIntersections.isEmpty()) {
                for (Intersection intersection : runningIntersections) {
                    trafficSignalTaskManager.startTask(intersection.getIdName(), intersection.getAutoRunStatus());
                }
            }
        } catch (Exception exception) {
            throw new TrafficLightServiceException(exception.getMessage(), exception);
        }
    }
}
