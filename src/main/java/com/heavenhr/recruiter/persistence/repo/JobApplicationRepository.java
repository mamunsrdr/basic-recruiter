package com.heavenhr.recruiter.persistence.repo;

import com.heavenhr.recruiter.persistence.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
}
