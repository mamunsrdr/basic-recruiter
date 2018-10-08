package com.heavenhr.recruiter.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "JobApplicationDto")
public class JobApplicationDto {

    @ApiModelProperty(value = "id (unique)", dataType = "number", example = "1")
    private Long id;

    @ApiModelProperty(value = "candidateEmail", dataType = "string", example = "someone@example.com")
    private String candidateEmail;

    @ApiModelProperty(value = "resumeText", dataType = "string", example = "Resume text description")
    private String resumeText;

    @ApiModelProperty(value = "resumeText", dataType = "string", example = "APPLIED")
    private ApplicationStatus applicationStatus;


    @ApiModelProperty(value = "jobOfferId", dataType = "number", example = "1")
    private Long jobOfferId;

    /**
     * convert domain object to dto
     *
     * @param jobApplication {@link JobOffer}
     * @return {@link JobOfferDto}
     */
    public static JobApplicationDto convert(JobApplication jobApplication) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        JobApplicationDto applicationDto = modelMapper.map(jobApplication, JobApplicationDto.class);
        applicationDto.setJobOfferId(jobApplication.getJobOffer().getId());
        return applicationDto;
    }
}
