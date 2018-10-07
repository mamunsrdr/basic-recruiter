package com.heavenhr.recruiter.test.unit.controller;

import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.controller.JobApplicationController;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import com.heavenhr.recruiter.service.application.JobApplicationService;
import com.heavenhr.recruiter.service.offer.JobOfferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JobApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobOfferService jobOfferService;

    @MockBean
    private JobApplicationService jobApplicationService;


    @Test
    public void testGetJobApplication() throws Exception {
        JobApplication jobApplication = JobApplication.builder()
                .id(1L)
                .candidateEmail("someone@email.com")
                .resumeText("Resume text")
                .applicationStatus(ApplicationStatus.APPLIED)
                .jobOffer(JobOffer.builder().id(1L).build())
                .build();
        given(jobApplicationService.getJobApplicationById(1L)).willReturn(jobApplication);

        this.mockMvc.perform(get("/rest/v1/application/" + jobApplication.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jobApplication.getId()))
                .andExpect(jsonPath("$.candidateEmail").value(jobApplication.getCandidateEmail()))
                .andExpect(jsonPath("$.resumeText").value(jobApplication.getResumeText()))
                .andExpect(jsonPath("$.jobOfferId").value(1))
                .andExpect(jsonPath("$.applicationStatus").value(jobApplication.getApplicationStatus().name()));
    }

}
