package com.tms.TaskManagementSystem.exception.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private Date timeStamp;
    private String error;
}
