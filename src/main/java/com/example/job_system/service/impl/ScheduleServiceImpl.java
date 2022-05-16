package com.example.job_system.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.job_system.config.TaskPool;
import com.example.job_system.entity.JobEntity;
import com.example.job_system.entity.JobStatus;
import com.example.job_system.repository.JobRepository;
import com.example.job_system.service.ScheduleService;
import com.example.job_system.service.task.ScheduleTaskCreator;
import com.example.job_system.service.task.Task;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final TaskScheduler taskScheduler;

    private final JobRepository jobRepository;

    private final ApplicationContext applicationContext;

    private final TaskPool taskPool;

    public ScheduleServiceImpl(
        TaskScheduler taskScheduler,
        JobRepository jobRepository,
        ApplicationContext applicationContext,
        TaskPool taskPool) {
        this.taskScheduler = taskScheduler;
        this.jobRepository = jobRepository;
        this.applicationContext = applicationContext;
        this.taskPool = taskPool;
    }

    @Override
    public void scheduleJob(JobEntity job) {
        Trigger trigger = new PeriodicTrigger(job.getPeriod(), TimeUnit.HOURS);
        queueJob(job, trigger);

        ScheduleTaskCreator creator = applicationContext.getBean(ScheduleTaskCreator.class, job.getId(), trigger);
        ScheduledFuture<?> task = taskScheduler.schedule(creator, trigger);
        creator.setScheduledFuture(task);
        taskPool.getScheduledTasks().add(creator);
    }

    @Override
    public void cancelJob(String jobId) {
        // Cancel ScheduleTaskCreator - stop periodical start of Tasks
        JobEntity job = jobRepository.findById(jobId).orElseThrow();
        List<ScheduleTaskCreator> creators = taskPool.getScheduledTasks()
            .stream()
            .filter(it -> Objects.equals(it.getJobId(), jobId))
            .collect(Collectors.toList());
        for (ScheduleTaskCreator creator : creators) {
            creator.cancel();
        }

        // Remove job from queue with blocked tasks
        Queue<Task> queue = taskPool.getGroupedTasks().get(job.getFullGroupKey());
        if (queue != null) {
            queue.removeIf(task -> Objects.equals(task.getJobId(), jobId));
        }

        // Remove job from thread pool queue
        taskPool.getTaskExecutorWorkQueue()
            .stream()
            .filter(it -> it instanceof Task)
            .map(it -> (Task) it)
            .filter(it -> Objects.equals(it.getJobId(), jobId))
            .findAny()
            .ifPresent(it -> taskPool.getTaskExecutorWorkQueue().remove(it));

        // Stop running tasks
        taskPool.getRunningTasks()
            .stream()
            .filter(it -> Objects.equals(it.getJobId(), jobId))
            .forEach(Task::abort);
    }

    @Transactional
    void queueJob(JobEntity job, Trigger trigger) {
        job.addHistoryEntry(trigger.nextExecutionTime(new SimpleTriggerContext()));
        job.getLatestHistory().setStatus(JobStatus.QUEUED);
        jobRepository.save(job);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    protected ScheduleTaskCreator createScheduleTask(
        String jobId,
        Trigger trigger) {
        return new ScheduleTaskCreator(jobId, trigger, taskPool);
    }
}
