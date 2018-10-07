package com.heavenhr.recruiter.persistence.repo;

import com.heavenhr.recruiter.persistence.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    public Long countByJobOffer_Id(Long offerId);

    public Long countByJobOffer_IdAndCandidateEmail(Long offerId, String candidateEmail);

    public List<JobApplication> findAllByJobOffer_Id(Long offerId);
}
