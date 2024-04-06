package com.tms.TaskManagementSystem.entity.enums;

import lombok.Getter;

@Getter
public enum TaskStatus {
    TO_DO(0, "TO_DO"),
    IN_PROGRESS(1, "IN_PROGRESS"),
    DONE(2, "DONE");

    private final int intValue;
    private final String stringValue;

    TaskStatus(final int intValue, final String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }
    public static TaskStatus definingStatus(int statusInt) {
        return switch (statusInt) {
            case 0 -> TO_DO;
            case 1 -> IN_PROGRESS;
            case 2 -> DONE;
            default -> null;
        };
    }
}