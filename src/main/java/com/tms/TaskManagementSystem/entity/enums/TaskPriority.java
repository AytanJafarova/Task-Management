package com.tms.TaskManagementSystem.entity.enums;

import lombok.Getter;

@Getter
public enum TaskPriority {
    LOW(0,"LOW"),
    MEDIUM(1,"MEDIUM"),
    HIGH(2,"HIGH");

    private final int intValue;
    private final String stringValue;

    TaskPriority(int intValue, String stringValue)
    {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }
    public static TaskPriority definingDegree(int degreeInt)
    {
        return switch (degreeInt) {
            case 0 -> LOW;
            case 1 -> MEDIUM;
            case 2 -> HIGH;
            default -> null;
        };
    }
}
