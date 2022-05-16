package com.example.job_system.controller;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.job_system.config.TaskPool;
import com.example.job_system.dto.JobStats;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/stats")
public class JobStatsController {

    private final TaskPool taskPool;

    public JobStatsController(TaskPool taskPool) {
        this.taskPool = taskPool;
    }

    @Operation(summary = "Collect all statistics")
    @ApiResponse(responseCode = "200", description = "Statistics collected",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = JobStats.class)) })
    @GetMapping
    public JobStats collect() {
        JobStats stats = new JobStats();
        stats.setSizeOfQueue(taskPool.getTaskExecutorWorkQueue().size());
        stats.setRunningJobs(taskPool.getRunningTasks().size() - stats.getSizeOfQueue());

        int totalBlockedJobs = taskPool.getGroupedTasks()
            .values()
            .stream()
            .mapToInt(Collection::size)
            .sum();
        stats.setTotalBlockedJobs(totalBlockedJobs);

        return stats;
    }
}
