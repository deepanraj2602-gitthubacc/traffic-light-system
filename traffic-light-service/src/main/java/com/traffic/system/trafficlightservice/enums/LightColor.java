
package com.traffic.system.trafficlightservice.enums;

import java.util.Arrays;
import java.util.Objects;

public enum LightColor {
    RED, GREEN, YELLOW;

    public static LightColor getEnumType(String colorStr) {
        return Arrays.stream(LightColor.values()).filter(color -> color.name().equals(colorStr))
                .findFirst().orElseThrow();
    }

    public static String getEnumString(LightColor colorEnum) {
        return Objects.nonNull(colorEnum) ? colorEnum.name() : null;
    }
}
