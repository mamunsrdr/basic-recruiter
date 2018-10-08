package com.heavenhr.recruiter.test.dto;

import com.heavenhr.recruiter.app.dto.JobApplicationDto;
import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobApplicationDtoTest {

    private JobApplication jobApplication;

    @Before
    public void setup() {
        jobApplication = JobApplication.builder()
                .id(1L)
                .candidateEmail("someone1@email.com")
                .resumeText("Resume text 1")
                .applicationStatus(ApplicationStatus.APPLIED)
                .jobOffer(JobOffer.builder().id(1L).build())
                .build();
    }

    @Test
    public void testConversionToDto() throws Exception {
        JobApplicationDto applicationDto = JobApplicationDto.convert(jobApplication);
        assertThat(applicationDto.getId(), is(jobApplication.getId()));
        assertThat(applicationDto.getJobOfferId(), is(jobApplication.getJobOffer().getId()));
        assertThat(applicationDto.getApplicationStatus(), is(jobApplication.getApplicationStatus()));
        assertThat(applicationDto.getCandidateEmail(), is(jobApplication.getCandidateEmail()));
        assertThat(applicationDto.getResumeText(), is(jobApplication.getResumeText()));
    }
}
