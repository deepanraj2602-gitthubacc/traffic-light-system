
package com.traffic.system.trafficlightservice.enums;

import java.util.Arrays;
import java.util.Objects;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public static Direction getEnumType(String directionStr) {
        return Arrays.stream(Direction.values()).filter(dir -> dir.name().equals(directionStr))
                .findFirst().orElseThrow();
    }

    public static String getEnumString(Direction directionEnum) {
        return Objects.nonNull(directionEnum) ? directionEnum.name() : null;
    }
}
