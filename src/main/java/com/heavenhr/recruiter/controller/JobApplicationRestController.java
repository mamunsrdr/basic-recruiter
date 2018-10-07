package com.heavenhr.recruiter.controller;

import com.heavenhr.recruiter.app.command.JobApplicationCommand;
import com.heavenhr.recruiter.app.dto.JobApplicationDto;
import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.service.application.JobApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/application")
@Api(value = "Job Application", description = "Job applications related api collection", tags = {"Job Application"})
public class JobApplicationRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobApplicationRestController.class);

    private final JobApplicationService jobApplicationService;

    @Autowired
    public JobApplicationRestController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Retrieve job application details by id")
    public ResponseEntity<JobApplicationDto> getJobApplication(@ApiParam("Job application id")
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
    public ResponseEntity<List<JobApplicationDto>> getAllJobApplicationByOffer(@ApiParam("Job offer id")
                                                                               @PathVariable("offerId") Long offerId) {
        List<JobApplication> jobApplications = this.jobApplicationService.getAllJobApplicationByOfferId(offerId);
        if (jobApplications.isEmpty()) {
            ResponseEntity.noContent().build();
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
    public ResponseEntity<Long> countJobApplication(@ApiParam("Job offer id")
                                                    @PathVariable Long offerId) {
        Long count = this.jobApplicationService.countJobApplicationByOfferId(offerId);
        return ResponseEntity.ok().body(count);
    }

    @PostMapping(
            path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Create job applications under a job offer")
    public ResponseEntity createJobApplication(@Valid @RequestBody JobApplicationCommand jobApplicationCommand, UriComponentsBuilder ucBuilder) {
        LOGGER.debug("create job application: " + jobApplicationCommand.toString());

        JobApplication jobApplication = JobApplicationCommand.convert(jobApplicationCommand);

        //check if already applied
        if (this.jobApplicationService.isCandidateApplied(jobApplication.getJobOffer().getId(), jobApplication.getCandidateEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        //save job application
        this.jobApplicationService.createJobApplication(jobApplication);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/{id}").buildAndExpand(jobApplication.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PatchMapping(
            path = "/updateStatus/{applicationId}/{applicationStatus}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Update the progress of job application status")
    public ResponseEntity updateJobApplicationStatus(@ApiParam("Job application id")
                                                     @PathVariable("applicationId") Long applicationId,
                                                     @ApiParam("Application status - APPLIED, INVITED, REJECTED, HIRED")
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
