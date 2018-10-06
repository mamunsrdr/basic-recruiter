package com.heavenhr.recruiter.persistence.repo;

import com.heavenhr.recruiter.persistence.entity.JobOffer;
import org.springframework.data.repository.CrudRepository;

public interface JobOfferRepository extends CrudRepository<JobOffer, Long> {
}
