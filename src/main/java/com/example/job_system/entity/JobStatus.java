package com.example.job_system.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public enum JobStatus {
    @Schema(description = "Job is scheduled for a specific time")
    SCHEDULED,

    @Schema(description = "Job have been queued and waiting for free spot to be executed")
    QUEUED,

    @Schema(description = "Job has been started and hasn't been done yet")
    RUNNING,

    @Schema(description = "Job finished successfully")
    SUCCESS,

    @Schema(description = "Job finished with an error")
    FAILED,
}
