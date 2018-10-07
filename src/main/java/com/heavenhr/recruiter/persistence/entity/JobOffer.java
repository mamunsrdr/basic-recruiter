package com.heavenhr.recruiter.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobOffer {

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
