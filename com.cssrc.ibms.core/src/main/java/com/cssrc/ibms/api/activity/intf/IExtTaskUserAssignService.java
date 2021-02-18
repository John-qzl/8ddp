package com.cssrc.ibms.api.activity.intf;

import java.util.List;
import com.cssrc.ibms.api.activity.model.ITaskExecutor;

public interface IExtTaskUserAssignService
{
    public List<?extends ITaskExecutor> getTaskExecutor(String actDefId,String defKey,String nodeId,String businessKey);
}
