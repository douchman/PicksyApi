package com.buck.vsplay.global.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class Timestamp {

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
