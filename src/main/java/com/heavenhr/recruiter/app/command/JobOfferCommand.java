package com.heavenhr.recruiter.app.command;

import com.heavenhr.recruiter.persistence.entity.JobOffer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "JobOfferCommand")
public class JobOfferCommand {

    @ApiModelProperty(value = "Job title (unique)", dataType = "string", required = true, example = "Senior java developer")
    @NotBlank
    private String jobTitle;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "Start date (format: yyyy-MM-dd)", dataType = "date", required = true, example = "2018-11-27")
    @NotNull
    private LocalDate startDate;

    /**
     * convert command object to entity
     *
     * @param jobOfferCommand {@link JobOfferCommand}
     * @return {@link JobOffer}
     */
    public static JobOffer convertToJobOfferEntity(JobOfferCommand jobOfferCommand) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        JobOffer jobOffer = modelMapper.map(jobOfferCommand, JobOffer.class);
        jobOffer.setTotalApplication(0L);
        return jobOffer;
    }
}
