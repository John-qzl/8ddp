package com.cssrc.ibms.core.user.event;

import org.springframework.context.ApplicationEvent;

public class TriggerNewFlowEvent extends ApplicationEvent
{
    private static final long serialVersionUID = 2591676062449086299L;
    
    public TriggerNewFlowEvent(TriggerNewFlowModel source)
    {
        super(source);
    }
}
