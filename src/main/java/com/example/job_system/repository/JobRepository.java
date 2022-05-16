package com.example.job_system.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.job_system.entity.JobEntity;

public interface JobRepository extends PagingAndSortingRepository<JobEntity, String> {
}
