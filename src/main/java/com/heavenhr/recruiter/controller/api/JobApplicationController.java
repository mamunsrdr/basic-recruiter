package com.heavenhr.recruiter.controller.api;

import com.heavenhr.recruiter.app.command.JobApplicationCommand;
import com.heavenhr.recruiter.app.dto.JobApplicationDto;
import com.heavenhr.recruiter.app.error.ResourceException;
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/application")
@Api(value = "Job Application", description = "Job applications related api collection", tags = {"Job Application"})
public class JobApplicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobApplicationController.class);

    private final MessageSourceAccessor messages;
    private final JobOfferService jobOfferService;
    private final JobApplicationService jobApplicationService;

    @Autowired
    public JobApplicationController(MessageSourceAccessor messages,
                                    JobOfferService jobOfferService,
                                    JobApplicationService jobApplicationService) {
        this.messages = messages;
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
            throw new ResourceException(HttpStatus.NOT_FOUND, messages.getMessage("resource.not.found", LocaleContextHolder.getLocale()));
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
    public ResponseEntity createJobApplication(@Valid @RequestBody JobApplicationCommand jobApplicationCommand) {
        LOGGER.debug("create job application: " + jobApplicationCommand.toString());

        //check if applied under valid offer
        JobApplication jobApplication = JobApplicationCommand.convert(jobApplicationCommand);
        JobOffer jobOffer = this.jobOfferService.getJobOfferById(jobApplication.getJobOffer().getId());
        if (jobOffer == null) {
            throw new ResourceException(HttpStatus.UNPROCESSABLE_ENTITY, messages.getMessage("offer.not.found", LocaleContextHolder.getLocale()));
        }
        jobApplication.setJobOffer(jobOffer);

        //check if already applied
        if (this.jobApplicationService.isCandidateApplied(jobApplication.getJobOffer().getId(), jobApplication.getCandidateEmail())) {
            throw new ResourceException(HttpStatus.CONFLICT, messages.getMessage("application.exists", LocaleContextHolder.getLocale()));
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
                                                     @ApiParam(value = "Application status", required = true)
                                                     @PathVariable("applicationStatus") ApplicationStatus applicationStatus) {
        JobApplication application = this.jobApplicationService.getJobApplicationById(applicationId);
        if (application == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, messages.getMessage("resource.not.found", LocaleContextHolder.getLocale()));
        }
        this.jobApplicationService.updateJobApplicationStatus(application, applicationStatus);
        return ResponseEntity.ok().build();
    }
}
