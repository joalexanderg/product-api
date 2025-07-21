package com.incomex.product_api.service.job;

import com.incomex.product_api.model.JobStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JobRegistry {

    private final Map<UUID, JobStatus> jobs = new ConcurrentHashMap<>();

    public UUID registerJob() {
        UUID jobId = UUID.randomUUID();
        jobs.put(jobId, JobStatus.PENDING);
        return jobId;
    }

    public void updateStatus(UUID jobId, JobStatus status) {
        jobs.put(jobId, status);
    }

    public JobStatus getStatus(UUID jobId) {
        return jobs.getOrDefault(jobId, null);
    }

    public void clear() {
        jobs.clear();
    }
}