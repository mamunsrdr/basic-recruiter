package com.heavenhr.recruiter.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class JobOffer extends AuditModel {

    @Id
    @GeneratedValue
    @Access(AccessType.PROPERTY)
    private Long id;
    private String jobTitle;
    private LocalDate startDate;
    private Long totalApplication;

    @OneToMany(
            mappedBy = "jobOffer",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<JobApplication> jobApplications;

}
