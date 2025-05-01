package com.buck.vsplay.global.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "BATCH_JOB_INSTANCE")
@Getter
@Setter
public class BatchJobInstance {

    @Id
    @Column(name = "job_instance_id")
    private Long id;

    @Column(name = "job_name")
    private String jobName;
}
