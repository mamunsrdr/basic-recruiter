package com.heavenhr.recruiter.test.dto;

import com.heavenhr.recruiter.app.dto.JobOfferDto;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobOfferDtoTest {

    private JobOffer jobOffer;

    @Before
    public void setup() {
        jobOffer = JobOffer.builder()
                .id(1L)
                .jobTitle("Senior java developer")
                .startDate(LocalDate.now())
                .totalApplication(0L)
                .build();
    }

    @Test
    public void testConversionToDto() throws Exception {
        JobOfferDto jobOfferDto = JobOfferDto.convert(jobOffer);
        assertThat(jobOfferDto.getId(), is(jobOffer.getId()));
        assertThat(jobOfferDto.getJobTitle(), is(jobOffer.getJobTitle()));
        assertThat(jobOfferDto.getStartDate(), is(jobOffer.getStartDate()));
        assertThat(jobOfferDto.getTotalApplication(), is(jobOffer.getTotalApplication()));
    }
}
