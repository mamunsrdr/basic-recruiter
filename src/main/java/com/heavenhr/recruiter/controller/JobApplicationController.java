package com.heavenhr.recruiter.controller;

import com.heavenhr.recruiter.app.command.JobApplicationCommand;
import com.heavenhr.recruiter.app.dto.JobApplicationDto;
import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import com.heavenhr.recruiter.service.application.JobApplicationService;
import com.heavenhr.recruiter.service.offer.JobOfferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/application")
@Api(value = "Job Application", description = "Job applications related api collection", tags = {"Job Application"})
public class JobApplicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobApplicationController.class);

    private final JobOfferService jobOfferService;
    private final JobApplicationService jobApplicationService;

    @Autowired
    public JobApplicationController(JobOfferService jobOfferService,
                                    JobApplicationService jobApplicationService) {
        this.jobOfferService = jobOfferService;
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Retrieve job application details by id")
    public ResponseEntity<JobApplicationDto> getJobApplication(@ApiParam(value = "Job application id", required = true)
                                                               @PathVariable("id") Long id) {
        JobApplication application = this.jobApplicationService.getJobApplicationById(id);
        if (application == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(JobApplicationDto.convert(application));
    }

    @GetMapping(
            path = "/getAllByOffer/{offerId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Retrieve all job applications by job offer id")
    public ResponseEntity<List<JobApplicationDto>> getAllJobApplicationByOffer(@ApiParam(value = "Job offer id", required = true)
                                                                               @PathVariable("offerId") Long offerId) {
        List<JobApplication> jobApplications = this.jobApplicationService.getAllJobApplicationByOfferId(offerId);
        if (jobApplications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(
                jobApplications.stream()
                        .map(JobApplicationDto::convert)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping(
            path = "/countByOffer/{offerId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Count number of job applications by job offer id")
    public ResponseEntity<Long> countJobApplication(@ApiParam(value = "Job offer id", required = true)
                                                    @PathVariable Long offerId) {
        Long count = this.jobApplicationService.countJobApplicationByOfferId(offerId);
        return ResponseEntity.ok().body(count);
    }

    @PostMapping()
    @ApiOperation("Create job applications under a job offer")
    public ResponseEntity createJobApplication(@Valid @RequestBody JobApplicationCommand jobApplicationCommand, UriComponentsBuilder ucBuilder) {
        LOGGER.debug("create job application: " + jobApplicationCommand.toString());

        //check if applied under valid offer
        JobOffer jobOffer = this.jobOfferService.getJobOfferById(jobApplicationCommand.getJobOfferId());
        if (jobOffer == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        JobApplication jobApplication = JobApplicationCommand.convert(jobApplicationCommand);
        jobApplication.setJobOffer(jobOffer);

        //check if already applied
        if (this.jobApplicationService.isCandidateApplied(jobApplication.getJobOffer().getId(), jobApplication.getCandidateEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        //save job application
        this.jobApplicationService.createJobApplication(jobApplication);

        URI getUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(jobApplication.getId()).toUri();
        return ResponseEntity.created(getUri).build();
    }

    @PatchMapping(
            path = "/updateStatus/{applicationId}/{applicationStatus}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Update the progress of job application status")
    public ResponseEntity updateJobApplicationStatus(@ApiParam(value = "Job application id", required = true)
                                                     @PathVariable("applicationId") Long applicationId,
                                                     @ApiParam(value = "Application status - APPLIED, INVITED, REJECTED, HIRED", required = true)
                                                     @PathVariable("applicationStatus") ApplicationStatus applicationStatus) {
        JobApplication application = this.jobApplicationService.getJobApplicationById(applicationId);
        if (application == null) {
            return ResponseEntity.notFound().build();
        } else if (applicationStatus == null) {
            return ResponseEntity.badRequest().build();
        }
        this.jobApplicationService.updateJobApplicationStatus(application, applicationStatus);
        return ResponseEntity.ok().build();
    }
}
