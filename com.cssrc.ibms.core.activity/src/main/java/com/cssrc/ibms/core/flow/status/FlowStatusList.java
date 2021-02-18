package com.cssrc.ibms.core.flow.status;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bpm")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlowStatusList
{
    @XmlElementWrapper(name = "task")
    @XmlElements({ @XmlElement(name = "statu", type = FlowStatus.class) })
    private List<FlowStatus> status;

    public List<FlowStatus> getStatus()
    {
        return status;
    }

    public void setStatus(List<FlowStatus> status)
    {
        this.status = status;
    }

  
}
