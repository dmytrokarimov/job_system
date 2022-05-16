# Job system

## System requirements
* mvn
* java 17

## How to run
* Build project using
```
mvn clean install
```
* Take jar from `target/` folder
* Run jar using java -jar <jarname>
* Open openapi using http://localhost:8080/swagger-ui.html and call required endpoints

## Requirements

### Jobs Definitions:
* The system should support custom job types. The different job types will be defined by the developers who use this system. 
* A job should have a life-cycle that can be tracked i.e. we need to track the job's current state - running, failed etc.
* Uniqueness - define a unique identifier for each job. Two jobs with the same unique identifier and the same type cannot run concurrently. A Job can run 
 concurrently with other jobs of a different type.
* Cancellations - have a mechanism that notifies jobs they need to abort and clean up.
  introspection - fetch current system stats e.g. all running jobs, size of queue, etc.

### Scheduling:
* Jobs can be scheduled for periodic execution (every 1, 2, 6, or 12 hours). 
* Jobs can be executed immediately for a one time run.
### Concurrency:
* Jobs need to run concurrently. A job should not wait for a previous job to finish before it can run. You can assume that there are 
 no dependencies between any two jobs. 
* There should be a limit on the amount of jobs that can run concurrently at any given moment. If the limit is reached the pending job(s) 
 should wait for an open spot.

