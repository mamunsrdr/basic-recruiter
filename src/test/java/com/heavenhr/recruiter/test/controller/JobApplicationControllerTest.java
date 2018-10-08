package com.heavenhr.recruiter.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heavenhr.recruiter.app.command.JobApplicationCommand;
import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
import com.heavenhr.recruiter.service.application.JobApplicationService;
import com.heavenhr.recruiter.service.offer.JobOfferService;
import org.junit.Before;
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
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    private JobApplication jobApplication1;
    private JobApplication jobApplication2;
    private final JobOffer jobOffer = JobOffer.builder().id(1L).build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private JobApplicationCommand jobApplicationCommand;

    @Before
    public void setup() {
        jobApplication1 = JobApplication.builder()
                .id(1L)
                .candidateEmail("someone1@email.com")
                .resumeText("Resume text 1")
                .applicationStatus(ApplicationStatus.APPLIED)
                .jobOffer(jobOffer)
                .build();
        jobApplication2 = JobApplication.builder()
                .id(2L)
                .candidateEmail("someone2@email.com")
                .resumeText("Resume text 2")
                .applicationStatus(ApplicationStatus.APPLIED)
                .jobOffer(jobOffer)
                .build();
        jobApplicationCommand = JobApplicationCommand.builder()
                .jobOfferId(1L)
                .candidateEmail("someone@example.com")
                .resumeText("Resume text")
                .build();
    }

    @Test
    public void testGetJobApplication() throws Exception {
        given(jobApplicationService.getJobApplicationById(1L)).willReturn(jobApplication1);

        this.mockMvc.perform(get("/rest/v1/application/" + jobApplication1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jobApplication1.getId()))
                .andExpect(jsonPath("$.candidateEmail").value(jobApplication1.getCandidateEmail()))
                .andExpect(jsonPath("$.resumeText").value(jobApplication1.getResumeText()))
                .andExpect(jsonPath("$.jobOfferId").value(1))
                .andExpect(jsonPath("$.applicationStatus").value(jobApplication1.getApplicationStatus().name()));
    }

    @Test
    public void testGetJobApplicationNotFound() throws Exception {
        given(jobApplicationService.getJobApplicationById(1L)).willReturn(null);

        this.mockMvc.perform(get("/rest/v1/application/" + 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllJobApplicationByOffer() throws Exception {
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

    @Test
    public void testCountJobApplication() throws Exception {
        given(jobApplicationService.countJobApplicationByOfferId(1L)).willReturn(2L);

        this.mockMvc.perform(get("/rest/v1/application/countByOffer/" + 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    public void testCreateJobApplication() throws Exception {
        given(jobOfferService.getJobOfferById(1L)).willReturn(jobOffer);
        doNothing().when(jobApplicationService).createJobApplication(jobApplication1);

        this.mockMvc
                .perform(post("/rest/v1/application").contentType("application/json").content(objectMapper.writeValueAsString(jobApplicationCommand)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateJobApplicationUniqueCandidateError() throws Exception {
        given(jobOfferService.getJobOfferById(1L)).willReturn(jobOffer);
        given(jobApplicationService.isCandidateApplied(1L, "someone@example.com")).willReturn(true);
        doNothing().when(jobApplicationService).createJobApplication(jobApplication1);

        this.mockMvc
                .perform(post("/rest/v1/application").contentType("application/json").content(objectMapper.writeValueAsString(jobApplicationCommand)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreateJobApplicationInvalidOffer() throws Exception {
        given(jobOfferService.getJobOfferById(1L)).willReturn(null);
        doNothing().when(jobApplicationService).createJobApplication(jobApplication1);


        this.mockMvc
                .perform(post("/rest/v1/application").contentType("application/json").content(objectMapper.writeValueAsString(jobApplicationCommand)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testUpdateJobApplicationStatus() throws Exception {
        given(jobApplicationService.getJobApplicationById(1L)).willReturn(jobApplication1);
        doNothing().when(jobApplicationService).updateJobApplicationStatus(jobApplication1, ApplicationStatus.HIRED);

        this.mockMvc
                .perform(patch("/rest/v1/application/updateStatus/1/" + ApplicationStatus.HIRED.name())) // :)
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateJobApplicationStatusNotFound() throws Exception {
        given(jobApplicationService.getJobApplicationById(1L)).willReturn(null);

        this.mockMvc
                .perform(patch("/rest/v1/application/updateStatus/1/" + ApplicationStatus.HIRED.name())) // :)
                .andExpect(status().isNotFound());
    }


}
