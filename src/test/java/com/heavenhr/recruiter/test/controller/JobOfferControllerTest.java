package com.heavenhr.recruiter.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heavenhr.recruiter.app.command.JobApplicationCommand;
import com.heavenhr.recruiter.app.command.JobOfferCommand;
import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.persistence.entity.JobOffer;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JobOfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobOfferService jobOfferService;

    private JobOffer jobOffer1;
    private JobOffer jobOffer2;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        jobOffer1 = JobOffer.builder()
                .id(1L)
                .jobTitle("Senior java developer")
                .startDate(LocalDate.now())
                .totalApplication(0L)
                .build();
        jobOffer2 = JobOffer.builder()
                .id(2L)
                .jobTitle("Lead java developer")
                .startDate(LocalDate.now())
                .totalApplication(2L)
                .build();
    }

    @Test
    public void testGetJobOffer() throws Exception {
        given(jobOfferService.getJobOfferById(1L)).willReturn(jobOffer1);

        this.mockMvc.perform(get("/rest/v1/offer/" + jobOffer1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jobOffer1.getId()))
                .andExpect(jsonPath("$.jobTitle").value(jobOffer1.getJobTitle()))
                .andExpect(jsonPath("$.startDate").value(jobOffer1.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.totalApplication").value(0));
    }

    @Test
    public void testGetJobOfferNotFound() throws Exception {
        given(jobOfferService.getJobOfferById(1L)).willReturn(null);

        this.mockMvc.perform(get("/rest/v1/offer/" + 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllJobOffer() throws Exception {
        given(jobOfferService.getAllJobOffer()).willReturn(Arrays.asList(jobOffer1, jobOffer2));

        this.mockMvc.perform(get("/rest/v1/offer/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].jobTitle").isString())
                .andExpect(jsonPath("$.[0].startDate").isString())
                .andExpect(jsonPath("$.[0].totalApplication").isNumber());
    }

    @Test
    public void testGetAllJobOfferEmptyList() throws Exception {
        given(jobOfferService.getAllJobOffer()).willReturn(Collections.emptyList());

        this.mockMvc.perform(get("/rest/v1/offer/list"))
                .andExpect(status().isNoContent());
    }


    @Test
    public void testCreateJobOffer() throws Exception {
        doNothing().when(jobOfferService).createJobOffer(jobOffer1);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jobTitle", "Senior java developer");
        requestBody.put("startDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        this.mockMvc
                .perform(post("/rest/v1/offer").contentType("application/json").content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateJobOfferUniqueTitleError() throws Exception {
        given(jobOfferService.isTitleExists("Senior java developer")).willReturn(true);
        doNothing().when(jobOfferService).createJobOffer(jobOffer1);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jobTitle", "Senior java developer");
        requestBody.put("startDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        this.mockMvc
                .perform(post("/rest/v1/offer").contentType("application/json").content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isConflict());
    }

}
