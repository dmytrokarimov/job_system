package com.example.job_system.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Column(name = "time_create", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "time_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @PrePersist
    private void save() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    @PreUpdate
    private void update() {
        this.updateTime = new Date();
    }

}
