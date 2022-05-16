package com.example.job_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class JobStats {

    @Schema(description = "Contains count of total running jobs")
    private int runningJobs;

    @Schema(description = "Contains count of total jobs blocked by the same type/group id")
    private int totalBlockedJobs;

    @Schema(description = "Contains count of total jobs waiting spot in the queue")
    private int sizeOfQueue;
}
