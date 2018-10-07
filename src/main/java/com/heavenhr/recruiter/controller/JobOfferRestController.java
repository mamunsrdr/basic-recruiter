package com.heavenhr.recruiter.controller;

import com.heavenhr.recruiter.app.command.JobOfferCommand;
import com.heavenhr.recruiter.app.dto.JobOfferDto;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import com.heavenhr.recruiter.service.offer.JobOfferService;
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
@RequestMapping("/rest/v1/offer")
@Api(value = "Job Offer", description = "Job offer related api collection", tags = {"Job Offer"})
public class JobOfferRestController {

    private final JobOfferService jobOfferService;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobOfferRestController.class);

    @Autowired
    public JobOfferRestController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Retrieve job offer details by id")
    public ResponseEntity<JobOfferDto> getJobOffer(@ApiParam("Job offer id")
                                                   @PathVariable("id") Long offerId) {
        JobOffer jobOffer = this.jobOfferService.getJobOfferById(offerId);

        //if no entity found with provided id
        if (jobOffer == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(JobOfferDto.convert(jobOffer));
        }
    }

    @GetMapping(
            path = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Retrieve all job offers")
    public ResponseEntity<List<JobOfferDto>> getAllJobOffer() {
        List<JobOffer> jobOfferList = this.jobOfferService.getAllJobOffer();

        //if list is empty
        if (jobOfferList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(
                jobOfferList.stream()
                        .map(JobOfferDto::convert)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping(
            path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Create job offer")
    public ResponseEntity createJobOffer(@Valid @RequestBody JobOfferCommand jobOfferCommand, UriComponentsBuilder ucBuilder) {
        LOGGER.debug("create offer: " + jobOfferCommand.toString());
        JobOffer jobOffer = JobOfferCommand.convertToJobOfferEntity(jobOfferCommand);

        //title unique check
        if (this.jobOfferService.isTitleExists(jobOffer.getJobTitle())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        //save job offer
        this.jobOfferService.createJobOffer(jobOffer);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/{id}").buildAndExpand(jobOffer.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


}
