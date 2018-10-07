package com.heavenhr.recruiter.service.application;

import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.event.publisher.JobAppStatusEventPub;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.persistence.repo.JobApplicationRepository;
import com.heavenhr.recruiter.persistence.repo.JobOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobAppStatusEventPub jobAppStatusEventPub;

    private final JobApplicationRepository jobApplicationRepository;
    private final JobOfferRepository jobOfferRepository;

    @Autowired
    public JobApplicationServiceImpl(JobAppStatusEventPub jobAppStatusEventPub,
                                     JobApplicationRepository jobApplicationRepository,
                                     JobOfferRepository jobOfferRepository) {
        this.jobAppStatusEventPub = jobAppStatusEventPub;
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobOfferRepository = jobOfferRepository;
    }

    @Override
    public JobApplication getJobApplicationById(Long id) {
        return this.jobApplicationRepository.findById(id).orElse(null);
    }

    @Override
    public List<JobApplication> getAllJobApplicationByOfferId(Long jobOfferId) {
        return this.jobApplicationRepository.findAllByJobOffer_Id(jobOfferId);
    }

    @Override
    public Long countJobApplicationByOfferId(Long jobOfferId) {
        return this.jobApplicationRepository.countByJobOffer_Id(jobOfferId);
    }

    @Override
    public boolean isCandidateApplied(Long jobOfferId, String candidateEmail) {
        return this.jobApplicationRepository.countByJobOffer_IdAndCandidateEmail(jobOfferId, candidateEmail) > 0;
    }

    @Override
    public void createJobApplication(JobApplication jobApplication) {
        this.jobApplicationRepository.save(jobApplication);
        //increase application count
        this.jobOfferRepository.increaseNumOfApplication(jobApplication.getJobOffer().getId());
    }

    @Override
    public void updateJobApplicationStatus(JobApplication jobApplication, ApplicationStatus newStatus) {
        ApplicationStatus oldStatus = jobApplication.getApplicationStatus();
        //update status in db
        jobApplication.setApplicationStatus(newStatus);
        this.jobApplicationRepository.save(jobApplication);

        //public status change event
        this.jobAppStatusEventPub.publishStatusChangeEvent(jobApplication.getId(), oldStatus, newStatus);
    }
}
