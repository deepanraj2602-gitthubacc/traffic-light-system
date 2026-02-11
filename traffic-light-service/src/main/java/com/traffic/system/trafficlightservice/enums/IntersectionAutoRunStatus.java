
package com.traffic.system.trafficlightservice.enums;

import java.util.Objects;

public enum IntersectionAutoRunStatus {
    RUNNING, PAUSED;

    public static IntersectionAutoRunStatus getEnumType(String stateStr) {
        return switch (stateStr) {
            case "pause" -> PAUSED;
            case null -> null;
            default -> RUNNING;
        };
    }

    public static String getEnumString(IntersectionAutoRunStatus statusEnum) {
        return Objects.nonNull(statusEnum) ? statusEnum.name() : null;
    }
}
