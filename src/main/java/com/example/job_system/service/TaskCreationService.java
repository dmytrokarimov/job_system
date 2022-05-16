package com.example.job_system.service;

import java.util.function.Consumer;

import com.example.job_system.service.task.ScheduleTaskCreator;
import com.example.job_system.service.task.Task;
import com.example.job_system.service.task.TaskResult;

public interface TaskCreationService {

    Task createTask(String jobId,
        String type,
        String groupId,
        ScheduleTaskCreator creator);
}
