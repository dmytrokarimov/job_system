package com.example.job_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.job_system.dto.JobDto;
import com.example.job_system.dto.JobModel;
import com.example.job_system.entity.JobEntity;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(source = "latestHistory.status", target = "status")
    @Mapping(source = "latestHistory.nextRun", target = "nextRun")
    JobDto toDto(JobEntity entity);

    JobEntity fromModel(JobModel model);
}
