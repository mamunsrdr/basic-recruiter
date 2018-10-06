package com.heavenhr.recruiter.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v1/offer")
public class JobOfferRestController {

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Retrieve job offer details by id")
    public void getJobOffer(@ApiParam("Job offer id")
                            @PathVariable Long offerId) {

    }

    @GetMapping(
            path = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Retrieve all job offer")
    public void getAllJobOffer() {

    }

    @PostMapping(
            path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Create job offer")
    public void createJobOffer() {

    }
}
