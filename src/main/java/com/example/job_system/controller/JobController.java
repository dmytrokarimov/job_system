package com.example.job_system.controller;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.job_system.dto.JobDto;
import com.example.job_system.dto.JobModel;
import com.example.job_system.exception.EntityNotFoundException;
import com.example.job_system.service.JobService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/job")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @Operation(summary = "Get a job by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found job",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = JobDto.class)) }),
        @ApiResponse(responseCode = "404", description = "Job not found", content = @Content) })
    @GetMapping("/{id}")
    public JobDto findById(@PathVariable String id) {
        return jobService.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    @Operation(summary = "Get all jobs")
    @ApiResponse(responseCode = "200", description = "Found jobs",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = JobDto.class)) })
    @GetMapping("/")
    public Page<JobDto> findAll(@ParameterObject Pageable pageable) {
        return jobService.findAll(pageable);
    }

    @Operation(summary = "Schedule job to be executed with specified period of tie")
    @ApiResponse(responseCode = "201", description = "Job created and scheduled",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = JobDto.class)) })
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public JobDto updateBook(@Valid @RequestBody JobModel job) {
        return jobService.scheduleJob(job);
    }

    @Operation(summary = "Cancel a job by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Job cancelled (if needed) and removed",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = JobDto.class)) }),
        @ApiResponse(responseCode = "404", description = "Job not found", content = @Content) })
    @DeleteMapping("/{id}")
    public JobDto deleteById(@PathVariable String id) {
        return jobService.deleteById(id)
            .orElseThrow(EntityNotFoundException::new);
    }
}
