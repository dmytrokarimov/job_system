package com.example.job_system.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.job_system.entity.JobEntity;
import com.example.job_system.entity.JobHistoryEntity;
import com.example.job_system.entity.JobStatus;

public interface JobHistoryRepository extends CrudRepository<JobHistoryEntity, String> {

    boolean existsByTypeAndGroupIdAndStatus(String type, String groupId, JobStatus status);
}
