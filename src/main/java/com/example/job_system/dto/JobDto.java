package com.example.job_system.dto;

import java.util.Date;

import com.example.job_system.entity.JobStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class JobDto {

    private String id;

    private String type;

    @Schema(description = "Two jobs with the same unique identifier and the same type cannot run concurrently. "
        + "A Job can run concurrently with other jobs of a different type.")
    private String groupId;

    private int period;

    private JobStatus status;

    private Date nextRun;

}
