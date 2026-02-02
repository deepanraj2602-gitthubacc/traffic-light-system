
package com.traffic.system.trafficlightservice.enums;

public enum IntersectionAutoRunStatus {
    RUNNING, PAUSED;

    public static IntersectionAutoRunStatus getEnum(String stateStr) {
        return switch (stateStr) {
            case "pause" -> PAUSED;
            default -> RUNNING;
        };
    }
}
