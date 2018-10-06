package com.heavenhr.recruiter.persistence.repo;

import com.heavenhr.recruiter.persistence.entity.JobApplication;
import org.springframework.data.repository.CrudRepository;

public interface JobApplicationRepository extends CrudRepository<JobApplication, Long> {
}
