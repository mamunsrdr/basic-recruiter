package com.heavenhr.recruiter.app.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@ApiModel(value = "JobOfferCommand")
public class JobOfferCommand {

    @ApiModelProperty(value = "Job title (unique)", dataType = "string", required = true, example = "Senior java developer")
    private String jobTitle;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "Start date (format: yyyy-MM-dd)", dataType = "date", required = true, example = "2018-11-27")
    private LocalDate startDate;
}
