package com.cssrc.ibms.core.resources.io.bean;

import com.cssrc.ibms.system.model.SysFile;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "RangeTestPlanBeanOuter")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeTestPlanBeanOuter {

    @XmlElement(name="rangeTestPlanBean")
    @XmlElementWrapper(name="rangeTestPlanBeanList")
    List<RangeTestPlanBean> rangeTestPlanBeanList;

    public List<RangeTestPlanBean> getRangeTestPlanBeanList() {
        return rangeTestPlanBeanList;
    }

    public void setRangeTestPlanBeanList(List<RangeTestPlanBean> rangeTestPlanBeanList) {
        this.rangeTestPlanBeanList = rangeTestPlanBeanList;
    }
}
