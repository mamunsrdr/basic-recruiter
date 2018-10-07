package com.heavenhr.recruiter.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.time.LocalDate;

@Getter
@Setter
@ApiModel(value = "JobOfferDto")
public class JobOfferDto {

    @ApiModelProperty(value = "id (unique)", dataType = "number", example = "1")
    private Long id;

    @ApiModelProperty(value = "jobTitle", dataType = "number", example = "Senior java developer")
    private String jobTitle;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "startDate (format: yyyy-MM-dd)", dataType = "date", example = "2018-11-27")
    private LocalDate startDate;

    @ApiModelProperty(value = "totalApplication", dataType = "number", example = "10")
    private Long totalApplication;

    /**
     * convert domain object to dto
     *
     * @param jobOffer {@link JobOffer}
     * @return {@link JobOfferDto}
     */
    public static JobOfferDto convert(JobOffer jobOffer) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(jobOffer, JobOfferDto.class);
    }
}
