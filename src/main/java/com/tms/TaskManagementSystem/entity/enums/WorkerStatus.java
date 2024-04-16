package com.tms.TaskManagementSystem.entity.enums;

import lombok.Getter;

@Getter
public enum WorkerStatus {
    INACTIVE(0, "INACTIVE"),
    ACTIVE(1, "ACTIVE");

    private final int intValue;
    private final String stringValue;

    WorkerStatus(final int intValue, final String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }
    public static WorkerStatus definingStatus(int statusInt) {
        return switch (statusInt) {
            case 0 -> INACTIVE;
            case 1 -> ACTIVE;
            default -> null;
        };
    }
}