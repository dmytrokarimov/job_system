package com.example.job_system.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.job_system.dto.JobDto;
import com.example.job_system.dto.JobModel;
import com.example.job_system.entity.JobEntity;
import com.example.job_system.mapper.JobMapper;
import com.example.job_system.repository.JobRepository;
import com.example.job_system.service.JobService;
import com.example.job_system.service.ScheduleService;

@Service
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;

    private final JobRepository jobRepository;

    private final ScheduleService scheduleService;

    @Autowired
    public JobServiceImpl(JobMapper jobMapper, JobRepository jobRepository, ScheduleService scheduleService) {
        this.jobMapper = jobMapper;
        this.jobRepository = jobRepository;
        this.scheduleService = scheduleService;
    }

    @Override
    public JobDto scheduleJob(JobModel model) {
        JobEntity entity = jobMapper.fromModel(model);

        scheduleService.scheduleJob(entity);

        entity = jobRepository.save(entity);
        return jobMapper.toDto(entity);
    }

    @Override
    public Optional<JobDto> findById(String id) {
        return jobRepository.findById(id)
            .map(jobMapper::toDto);
    }

    @Override
    public Optional<JobDto> deleteById(String id) {
        Optional<JobEntity> jobEntity = jobRepository.findById(id);
        jobEntity.ifPresent(it -> {
            scheduleService.cancelJob(it.getId());
            jobRepository.delete(it);
        });

        return jobEntity.map(jobMapper::toDto);
    }

    @Override
    public Page<JobDto> findAll(Pageable pageable) {
        return jobRepository.findAll(pageable)
            .map(jobMapper::toDto);
    }
}
