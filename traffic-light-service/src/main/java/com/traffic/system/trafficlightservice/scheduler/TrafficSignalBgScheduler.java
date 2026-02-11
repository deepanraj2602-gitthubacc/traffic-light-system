package com.traffic.system.trafficlightservice.scheduler;

import com.traffic.system.trafficlightservice.entity.Intersection;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.exception.TrafficLightServiceException;
import com.traffic.system.trafficlightservice.repository.IntersectionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@XSlf4j
@Component
@RequiredArgsConstructor
public class TrafficSignalBgScheduler {

    @NonNull
    private TrafficSignalTaskManager trafficSignalTaskManager;

    @NonNull
    private IntersectionRepository intersectionRepository;

    @Scheduled(fixedDelayString = "${app.scheduler.bgThreadPeriod}")
    public void runStartAndStopTaskBg() throws TrafficLightServiceException {

        try {
            List<Intersection> allIntersections = intersectionRepository.findAll();

            if (Objects.nonNull(allIntersections) && !allIntersections.isEmpty()) {
                for (Intersection intersection : allIntersections) {

                    // This start task running in background to
                    // ADD new / paused intersection with RUNNING status into scheduled running tasks of TrafficSignalTaskManager.,
                    if (!trafficSignalTaskManager.isTaskRunning(intersection.getIdName())
                            && intersection.getAutoRunStatus() == IntersectionAutoRunStatus.RUNNING) {
                        trafficSignalTaskManager.startTask(intersection.getIdName(), intersection.getAutoRunStatus());
                    }

                    // This stop task running in background to
                    // REMOVE existing / running intersection with PAUSED status into scheduled running tasks of TrafficSignalTaskManager.,
                    if (trafficSignalTaskManager.isTaskRunning(intersection.getIdName())
                            && intersection.getAutoRunStatus() == IntersectionAutoRunStatus.PAUSED) {
                        trafficSignalTaskManager.stopTask(intersection.getIdName(), intersection.getAutoRunStatus());
                    }
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new TrafficLightServiceException(exception.getMessage(), exception);
        }
    }
}
