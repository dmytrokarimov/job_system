package com.example.job_system.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.job_system.dto.JobDto;
import com.example.job_system.dto.JobModel;

public interface JobService {

    JobDto scheduleJob(JobModel model);

    Optional<JobDto> findById(String id);

    Optional<JobDto> deleteById(String id);

    Page<JobDto> findAll(Pageable pageable);
}
