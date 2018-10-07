package com.heavenhr.recruiter.test.unit.controller;

import com.heavenhr.recruiter.controller.JobApplicationController;
import com.heavenhr.recruiter.persistence.entity.JobApplication;
import com.heavenhr.recruiter.service.application.JobApplicationService;
import com.heavenhr.recruiter.service.offer.JobOfferService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(JobApplicationController.class)
public class JobApplicationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JobOfferService jobOfferService;

    @MockBean
    private JobApplicationService jobApplicationService;



    public void shouldGetJobApplication() throws Exception {
        JobApplication jobApplication = new JobApplication();
        given(jobApplicationService.getJobApplicationById(1L)).willReturn(jobApplication);
    }

}
