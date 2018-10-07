package com.heavenhr.recruiter.service.offer;

import com.heavenhr.recruiter.persistence.entity.JobOffer;

import java.util.List;

public interface JobOfferService {
    public JobOffer getJobOfferById(Long id);

    public List<JobOffer> getAllJobOffer();

    public boolean isTitleExists(String jobTitle);

    public void createJobOffer(JobOffer jobOffer);
}
