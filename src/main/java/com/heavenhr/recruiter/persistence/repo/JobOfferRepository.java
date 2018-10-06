package com.heavenhr.recruiter.persistence.repo;

import com.heavenhr.recruiter.persistence.entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    @Modifying
    @Query("update JobOffer jo set jo.totalApplication = jo.totalApplication + 1 WHERE jo.id = :jobOfferId")
    public void increaseNumOfApplication(@Param("jobOfferId") Long jobOfferId);

    public Long countByJobTitleEquals(String title);
}
