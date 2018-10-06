package com.heavenhr.recruiter.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel(value = "JobOfferDto")
public class JobOfferDto {

    @ApiModelProperty(value = "id (unique)", dataType = "number", example = "1")
    private Long id;

    @ApiModelProperty(value = "id (unique)", dataType = "number", example = "Senior java developer")
    private String jobTitle;

    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "startDate (format: yyyy-MM-dd)", dataType = "date", example = "2018-11-27")
    private LocalDate startDate;

    @ApiModelProperty(value = "totalApplication", dataType = "number", example = "10")
    private Long totalApplication;
}
