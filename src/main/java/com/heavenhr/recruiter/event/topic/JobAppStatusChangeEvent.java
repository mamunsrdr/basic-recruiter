package com.heavenhr.recruiter.event.topic;

import com.heavenhr.recruiter.app.type.ApplicationStatus;
import lombok.*;
import org.springframework.context.ApplicationEvent;

@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@Setter
public class JobAppStatusChangeEvent extends ApplicationEvent {

    private static final long serialVersionUID = -7269667954805697131L;

    private Long id;
    private ApplicationStatus oldStatus;
    private ApplicationStatus newStatus;

    public JobAppStatusChangeEvent(Object source) {
        super(source);
    }
}
