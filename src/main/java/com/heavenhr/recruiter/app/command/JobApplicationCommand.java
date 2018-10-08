package com.heavenhr.recruiter.app.command;

import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "JobApplicationCommand")
public class JobApplicationCommand {

    @ApiModelProperty(value = "Candidate email (unique per job offer)", dataType = "string", required = true, example = "someone@example.com")
    @NotBlank
    private String candidateEmail;

    @ApiModelProperty(value = "Candidate resume text", dataType = "string", required = true, example = "Resume text description...")
    @NotBlank
    private String resumeText;

    @ApiModelProperty(value = "job offer id", dataType = "number", required = true, example = "1")
    @NotNull
    private Long jobOfferId;

    /**
     * convert command object to entity
     *
     * @param jobApplicationCommand {@link JobApplicationCommand}
     * @return {@link JobApplication}
     */
    public static JobApplication convert(JobApplicationCommand jobApplicationCommand) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        JobApplication jobApplication = modelMapper.map(jobApplicationCommand, JobApplication.class);
        jobApplication.setApplicationStatus(ApplicationStatus.APPLIED);
        return jobApplication;
    }
}
