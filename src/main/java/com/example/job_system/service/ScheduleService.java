package com.example.job_system.service;

import com.example.job_system.entity.JobEntity;

public interface ScheduleService {

    void scheduleJob(JobEntity job);

    void cancelJob(String jobId);
}
