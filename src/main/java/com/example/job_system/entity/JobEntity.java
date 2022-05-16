package com.example.job_system.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JoinFormula;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "job")
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class JobEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @EqualsAndHashCode.Include
    private String id;

    @Column
    private String type;

    @Column
    private int period;

    @Column
    private String groupId;

    @OneToMany(mappedBy="jobEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<JobHistoryEntity> history = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinFormula("(SELECT h.id FROM job_history h WHERE h.job_id = id ORDER BY h.time_create DESC LIMIT 1)")
    private JobHistoryEntity latestHistory;

    public String getFullGroupKey() {
        return getType() + "_" + getGroupId();
    }

    public void addHistoryEntry(Date nextRun) {
        if (history == null) {
            history = new ArrayList<>();
        }
        JobHistoryEntity historyEntry = new JobHistoryEntity();
        historyEntry.setNextRun(nextRun);
        historyEntry.setStatus(JobStatus.SCHEDULED);
        historyEntry.setGroupId(this.getGroupId());
        historyEntry.setType(this.getType());

        history.add(historyEntry);
        historyEntry.setJobEntity(this);

        latestHistory = historyEntry;
    }
}
