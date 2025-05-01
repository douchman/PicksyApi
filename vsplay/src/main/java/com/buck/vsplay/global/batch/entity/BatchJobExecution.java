package com.buck.vsplay.global.batch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "BATCH_JOB_EXECUTION")
@Getter
@Setter
public class BatchJobExecution {
    @Id
    @Column(name = "JOB_EXECUTION_ID")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_instance_id")
    private BatchJobInstance batchJobInstance;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "status")
    private String status;
}
