package com.heavenhr.recruiter.test.unit.controller;

import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import com.heavenhr.recruiter.service.application.JobApplicationService;
import com.heavenhr.recruiter.service.offer.JobOfferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
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

    @Test
    public void testGetJobApplicationNotFound() throws Exception {
        given(jobApplicationService.getJobApplicationById(1L)).willReturn(null);

        this.mockMvc.perform(get("/rest/v1/application/" + 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllJobApplicationByOffer() throws Exception {
        JobApplication jobApplication1 = JobApplication.builder()
                .id(1L)
                .candidateEmail("someone1@email.com")
                .resumeText("Resume text 1")
                .applicationStatus(ApplicationStatus.APPLIED)
                .jobOffer(JobOffer.builder().id(1L).build())
                .build();
        JobApplication jobApplication2 = JobApplication.builder()
                .id(2L)
                .candidateEmail("someone2@email.com")
                .resumeText("Resume text 2")
                .applicationStatus(ApplicationStatus.APPLIED)
                .jobOffer(JobOffer.builder().id(1L).build())
                .build();
        given(jobApplicationService.getAllJobApplicationByOfferId(1L)).willReturn(Arrays.asList(jobApplication1, jobApplication2));

        this.mockMvc.perform(get("/rest/v1/application/getAllByOffer/" + 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].candidateEmail").isString())
                .andExpect(jsonPath("$.[0].resumeText").isString())
                .andExpect(jsonPath("$.[0].applicationStatus").isString())
                .andExpect(jsonPath("$.[0].jobOfferId").isNumber());
    }

    @Test
    public void testGetAllJobApplicationByOfferEmptyList() throws Exception {
        given(jobApplicationService.getAllJobApplicationByOfferId(1L)).willReturn(Collections.emptyList());

        this.mockMvc.perform(get("/rest/v1/application/getAllByOffer/" + 1L))
                .andExpect(status().isNoContent());
    }

}
