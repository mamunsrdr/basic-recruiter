package com.heavenhr.recruiter.persistence.entity;

import com.heavenhr.recruiter.app.type.ApplicationStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class JobApplication extends AuditModel {

    @Id
    @GeneratedValue
    private Long id;
    private String candidateEmail;
    @Column(columnDefinition = "TEXT")
    private String resumeText;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private JobOffer jobOffer;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
}
