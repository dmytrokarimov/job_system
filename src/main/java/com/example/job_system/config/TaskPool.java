package com.example.job_system.config;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.job_system.service.task.ScheduleTaskCreator;
import com.example.job_system.service.task.Task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TaskPool {

    /**
     * Contains all planned and pending jobs (pending due to pool size)
     */
    private final BlockingQueue<Runnable> taskExecutorWorkQueue;

    /**
     * Contains all scheduled tasks. ScheduleTaskCreator will produce new task depends on period configuration
     */
    private List<ScheduleTaskCreator> scheduledTasks = new CopyOnWriteArrayList<>();

    /**
     * Contains all blocked task due to uniq 'type + group id'.
     * <type + group id, Task queue>
     */
    private Map<String, Queue<Task>> groupedTasks = new ConcurrentHashMap<>();

    /**
     * Contains all currently running tasks
     */
    private List<Task> runningTasks = new CopyOnWriteArrayList<>();
}
