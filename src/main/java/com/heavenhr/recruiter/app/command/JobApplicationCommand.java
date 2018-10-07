package com.heavenhr.recruiter.app.command;

import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@ApiModel(value = "JobOfferCommand")
public class JobApplicationCommand {

    @ApiModelProperty(value = "Candidate email (unique per job offer)", dataType = "string", required = true, example = "someone@example.com")
    @NotBlank
    private String candidateEmail;

    @ApiModelProperty(value = "Candidate resume text", dataType = "string", required = true, example = "Resume text description...")
    @NotBlank
    private String resumeText;

    @ApiModelProperty(value = "job offer id", dataType = "number", required = true, example = "1")
    @NotBlank
    private Long jobOfferId;

    /**
     * convert command object to entity
     *
     * @param jobApplicationCommand {@link JobApplicationCommand}
     * @return {@link JobApplication}
     */
    public static JobApplication convert(JobApplicationCommand jobApplicationCommand) {
        ModelMapper modelMapper = new ModelMapper();
        JobApplication jobApplication = modelMapper.map(jobApplicationCommand, JobApplication.class);
        jobApplication.setApplicationStatus(ApplicationStatus.APPLIED);
        jobApplication.setJobOffer(
                JobOffer.builder()
                        .id(jobApplicationCommand.jobOfferId)
                        .build()
        );
        return jobApplication;
    }
}
