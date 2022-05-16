package com.example.job_system.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;

import com.example.job_system.dto.validation.OneOf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class JobModel {

    @NotBlank
    private String type;

    @Schema(description = "Two jobs with the same unique identifier and the same type cannot run concurrently. "
        + "A Job can run concurrently with other jobs of a different type.")
    @Nullable
    private String groupId;

    @Schema(description = "The period (in hours) that will be job ran with. Value '0' - job should be executed one time.")
    @OneOf({"0", "1", "2", "6", "12"})
    private int period;
}
