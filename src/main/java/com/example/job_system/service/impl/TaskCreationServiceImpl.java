package com.example.job_system.service.impl;

import java.util.function.Consumer;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.job_system.service.TaskCreationService;
import com.example.job_system.service.task.ScheduleTaskCreator;
import com.example.job_system.service.task.SimpleTask;
import com.example.job_system.service.task.Task;
import com.example.job_system.service.task.TaskResult;

@Service
public class TaskCreationServiceImpl implements TaskCreationService {

    private final ApplicationContext applicationContext;

    public TaskCreationServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Task createTask(String jobId, String type, String groupId, ScheduleTaskCreator creator) {
        return applicationContext.getBean(SimpleTask.class, jobId, type, groupId, creator);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected SimpleTask createSimpleTask(String jobId, String type, String groupId, ScheduleTaskCreator creator) {
        return new SimpleTask(jobId, type, groupId, creator);
    }
}
