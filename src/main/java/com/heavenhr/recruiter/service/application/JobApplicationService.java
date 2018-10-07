package com.heavenhr.recruiter.service.application;

import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.persistence.entity.JobApplication;

import java.util.List;

public interface JobApplicationService {
    public JobApplication getJobApplicationById(Long id);

    public List<JobApplication> getAllJobApplicationByOfferId(Long jobOfferId);

    public Long countJobApplicationByOfferId(Long jobOfferId);

    public boolean isCandidateApplied(Long jobOfferId, String candidateEmail);

    public void createJobApplication(JobApplication jobApplication);

    public void updateJobApplicationStatus(JobApplication jobApplication, ApplicationStatus newStatus);
}
