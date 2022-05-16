package com.example.job_system.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "job_history")
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class JobHistoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @EqualsAndHashCode.Include
    private String id;

    @Column
    private String type;

    @Column
    private String groupId;

    @Column
    private JobStatus status;

    @Column
    private Date nextRun;

    @Column
    private String errorMessage;

    @ManyToOne
    @JoinColumn(name="job_id", nullable=false)
    private JobEntity jobEntity;
}
