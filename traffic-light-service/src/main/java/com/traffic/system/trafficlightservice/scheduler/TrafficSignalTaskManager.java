package com.traffic.system.trafficlightservice.scheduler;

import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.service.TrafficLightService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@XSlf4j
@Component
@RequiredArgsConstructor
public class TrafficSignalTaskManager {

    @Value("${app.scheduler.task.initialDelay}")
    private Integer taskInitialDelay;       // Startup delay

    @Value("${app.scheduler.task.period}")
    private Integer taskPeriod;             // Run every period, ignoring execution time

    private final Map<String, ScheduledFuture<?>> runningTasks = new ConcurrentHashMap<>();
    @NonNull
    private ScheduledExecutorService scheduledExecutorService;
    @NonNull
    private TrafficLightService trafficLightService;

    @Synchronized
    public void startTask(String intersectionId, IntersectionAutoRunStatus autoRunStatus) {

        if (!runningTasks.containsKey(intersectionId) && autoRunStatus == IntersectionAutoRunStatus.RUNNING) {
            TrafficSignalTask trafficSignalTask = new TrafficSignalTask(intersectionId, autoRunStatus, trafficLightService);
            ScheduledFuture<?> scheduledFuture = scheduledExecutorService
                    .scheduleAtFixedRate(trafficSignalTask, taskInitialDelay, taskPeriod, TimeUnit.SECONDS);
            log.info("Running Task added: {}", intersectionId);
            runningTasks.put(intersectionId, scheduledFuture);
        }
    }

    @Synchronized
    public void stopTask(String intersectionId, IntersectionAutoRunStatus autoRunStatus) {

        if (runningTasks.containsKey(intersectionId) && autoRunStatus == IntersectionAutoRunStatus.PAUSED) {
            ScheduledFuture<?> scheduledFuture = runningTasks.remove(intersectionId);
            if (Objects.nonNull(scheduledFuture) && !scheduledFuture.isCancelled()) {
                scheduledFuture.cancel(true);
            }
            log.info("Running Task removed: {}", intersectionId);
        }
    }

    public boolean isTaskRunning(String intersectionId) {
        return runningTasks.containsKey(intersectionId);
    }
}
