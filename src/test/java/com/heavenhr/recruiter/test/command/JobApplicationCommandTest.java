package com.heavenhr.recruiter.test.command;

import com.heavenhr.recruiter.app.command.JobApplicationCommand;
import com.heavenhr.recruiter.app.dto.JobApplicationDto;
import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobApplicationCommandTest {

    private JobApplicationCommand jobApplicationCommand;

    @Before
    public void setup() {
        jobApplicationCommand = JobApplicationCommand.builder()
                .candidateEmail("someone1@email.com")
                .resumeText("Resume text 1")
                .jobOfferId(1L)
                .build();
    }

    @Test
    public void testConversionToDto() throws Exception {
        JobApplication jobApplication = JobApplicationCommand.convert(jobApplicationCommand);
        assertThat(jobApplication.getId(), nullValue());
        assertThat(jobApplication.getJobOffer().getId(), is(jobApplicationCommand.getJobOfferId()));
        assertThat(jobApplication.getApplicationStatus(), is(ApplicationStatus.APPLIED));
        assertThat(jobApplication.getCandidateEmail(), is(jobApplicationCommand.getCandidateEmail()));
        assertThat(jobApplication.getResumeText(), is(jobApplicationCommand.getResumeText()));
    }
}
