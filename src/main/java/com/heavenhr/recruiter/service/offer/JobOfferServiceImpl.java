package com.heavenhr.recruiter.service.offer;

import com.heavenhr.recruiter.persistence.entity.JobOffer;
import com.heavenhr.recruiter.persistence.repo.JobOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobOfferServiceImpl implements JobOfferService {

    private final JobOfferRepository jobOfferRepository;

    @Autowired
    public JobOfferServiceImpl(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    @Override
    public JobOffer getJobOfferById(Long id) {
        return this.jobOfferRepository.findById(id).orElse(null);
    }

    @Override
    public List<JobOffer> getAllJobOffer() {
        return this.jobOfferRepository.findAll();
    }

    @Override
    public boolean isTitleExists(String jobTitle) {
        return this.jobOfferRepository.countByJobTitleEquals(jobTitle) > 0;
    }

    @Override
    public void createJobOffer(JobOffer jobOffer) {
        this.jobOfferRepository.save(jobOffer);
    }
}
