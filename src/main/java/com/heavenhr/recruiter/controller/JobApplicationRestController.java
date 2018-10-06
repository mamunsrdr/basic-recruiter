package com.heavenhr.recruiter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v1/application")
@Api(value = "Job Application", description = "Job applications related api collection", tags = {"Job Application"})
public class JobApplicationRestController {

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Retrieve job application details by id")
    public void getJobApplication(@ApiParam("Job application id")
                                  @PathVariable Long id) {

    }

    @GetMapping(
            path = "/getAllByOffer/{offerId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Retrieve all job applications by job offer id")
    public void getAllJobApplicationByOffer(@ApiParam("Job offer id")
                                            @PathVariable Long offerId) {

    }

    @GetMapping(
            path = "/countByOffer/{offerId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Count number of job applications by job offer id")
    public void countJobApplication(@ApiParam("Job offer id")
                                            @PathVariable Long offerId) {

    }

    @PostMapping(
            path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Create job applications under a job offer")
    public void createJobApplication() {

    }

    @PatchMapping(
            path = "/updateStatus",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Update the progress of job application status")
    public void updateJobApplicationStatus() {

    }
}
