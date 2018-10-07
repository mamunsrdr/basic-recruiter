package com.heavenhr.recruiter.event.publisher;

import com.heavenhr.recruiter.app.type.ApplicationStatus;
import com.heavenhr.recruiter.event.topic.JobAppStatusChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class JobAppStatusEventPub {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public JobAppStatusEventPub(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * publish job application status change event
     *
     * @param id        {@link Long} - job application id
     * @param oldStatus {@link ApplicationStatus} - old status of application
     * @param newStatus {@link ApplicationStatus} - new status of application
     */
    public void publishStatusChangeEvent(Long id, ApplicationStatus oldStatus, ApplicationStatus newStatus) {
        JobAppStatusChangeEvent event = new JobAppStatusChangeEvent(this);
        event.setId(id);
        event.setOldStatus(oldStatus);
        event.setNewStatus(newStatus);
        applicationEventPublisher.publishEvent(event);
    }
}
