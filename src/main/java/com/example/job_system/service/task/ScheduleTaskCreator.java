package com.example.job_system.service.task;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.transaction.annotation.Transactional;

import com.example.job_system.config.TaskPool;
import com.example.job_system.entity.JobEntity;
import com.example.job_system.entity.JobStatus;
import com.example.job_system.repository.JobHistoryRepository;
import com.example.job_system.repository.JobRepository;
import com.example.job_system.service.TaskCreationService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ScheduleTaskCreator implements Runnable {

    @Getter
    private final String jobId;

    private final Trigger trigger;

    private final TaskPool taskPool;

    @Setter
    private ScheduledFuture<?> scheduledFuture;

    private String fullGroupKey;

    @Setter
    @Autowired
    private JobRepository jobRepository;

    @Setter
    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    @Setter
    @Autowired
    private ExecutorService taskExecutorService;

    @Setter
    @Autowired
    private TaskCreationService taskCreationService;

    @Override
    @Transactional
    public void run() {
        JobEntity job = null;
        try {
            job = jobRepository.findById(jobId).orElseThrow();
            fullGroupKey = job.getFullGroupKey();
            Task task = taskCreationService.createTask(job.getId(), job.getType(), job.getGroupId(), this);

            if (jobHistoryRepository.existsByTypeAndGroupIdAndStatus(job.getType(), job.getGroupId(), JobStatus.RUNNING)) {
                postponeTask(task);
            } else {
                job.getLatestHistory().setStatus(JobStatus.RUNNING);
                submitTask(task);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            storeFailedState(job, e);
        } finally {
            removeOneTimeJob(job);
        }
    }

    public void cancel() {
        scheduledFuture.cancel(true);
        taskPool.getScheduledTasks().remove(this);
    }

    private void submitTask(Task task) {
        Future<?> future = taskExecutorService.submit(task);
        task.setFuture(future);
        taskPool.getRunningTasks().add(task);
    }

    /**
     * This method works like callback.
     * This method updated status of run and submit postponed runs
     * @param result task execution result
     */
    public void processJobResult(Task finishedTask, TaskResult result) {
        try {
            taskPool.getRunningTasks().remove(finishedTask);

            JobEntity job = jobRepository.findById(jobId).orElseThrow();
            if (result.isSuccess()) {
                job.getLatestHistory().setStatus(JobStatus.SUCCESS);
            } else {
                job.getLatestHistory().setStatus(JobStatus.FAILED);
                job.getLatestHistory().setErrorMessage(result.getException().getMessage());
            }
            job.addHistoryEntry(trigger.nextExecutionTime(new SimpleTriggerContext()));

            runNextGroupTask();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void runNextGroupTask() {
        if (taskPool.getGroupedTasks().containsKey(fullGroupKey)) {
            Queue<Task> queue = taskPool.getGroupedTasks().get(fullGroupKey);
            if (!queue.isEmpty()) {
                Task nextTask = queue.poll();
                submitTask(nextTask);
            } else {
                taskPool.getGroupedTasks().remove(fullGroupKey);
            }
        }
    }

    private void postponeTask(Task task) {
        Queue<Task> queue = taskPool.getGroupedTasks().get(fullGroupKey);
        if (queue == null) {
            queue = new LinkedBlockingQueue<>();
            taskPool.getGroupedTasks().put(fullGroupKey, queue);
        }
        queue.add(task);
    }

    private void removeOneTimeJob(JobEntity job) {
        if (job != null && job.getPeriod() == 0) {
            cancel();
        }
    }

    private void storeFailedState(JobEntity job, Exception e) {
        if (job != null) {
            job.getLatestHistory().setStatus(JobStatus.FAILED);
            job.getLatestHistory().setErrorMessage(e.getMessage());
        }
    }
}
