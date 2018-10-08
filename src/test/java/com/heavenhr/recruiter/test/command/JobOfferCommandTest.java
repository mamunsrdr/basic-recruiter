package com.heavenhr.recruiter.test.command;

import com.heavenhr.recruiter.app.command.JobOfferCommand;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobOfferCommandTest {

    private JobOfferCommand jobOfferCommand;

    @Before
    public void setup() {
        jobOfferCommand = JobOfferCommand.builder()
                .jobTitle("Senior java developer")
                .startDate(LocalDate.now())
                .build();
    }

    @Test
    public void testConversionToDto() throws Exception {
        JobOffer jobOffer = JobOfferCommand.convertToJobOfferEntity(jobOfferCommand);
        assertThat(jobOffer.getId(), nullValue());
        assertThat(jobOffer.getJobTitle(), is(jobOfferCommand.getJobTitle()));
        assertThat(jobOffer.getStartDate(), is(jobOfferCommand.getStartDate()));
        assertThat(jobOffer.getTotalApplication(), is(0L));
    }
}
