package com.example.job_system.service.task;

import java.util.Date;
import java.util.Optional;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResult {

    private boolean success;

    private Date endDate;

    /**
     * Can be null in case if success is true
     */
    @Nullable
    private Exception exception;
}
