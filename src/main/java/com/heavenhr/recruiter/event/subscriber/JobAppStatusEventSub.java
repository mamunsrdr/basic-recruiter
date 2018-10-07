package com.heavenhr.recruiter.event.subscriber;

import com.heavenhr.recruiter.event.topic.JobAppStatusChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class JobAppStatusEventSub implements ApplicationListener<JobAppStatusChangeEvent> {

    private final Logger LOGGER = LoggerFactory.getLogger(JobAppStatusEventSub.class);

    @Override
    public void onApplicationEvent(JobAppStatusChangeEvent event) {
        LOGGER.debug("*************************************************");
        LOGGER.debug("Status change event received: " + event.toString());
        LOGGER.debug("*************************************************");
    }
}
