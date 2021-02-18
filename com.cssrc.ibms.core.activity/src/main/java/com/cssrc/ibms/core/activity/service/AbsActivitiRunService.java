package com.cssrc.ibms.core.activity.service;

import java.util.Map;
import com.cssrc.ibms.core.activity.model.ProcessCmd;

public abstract class AbsActivitiRunService implements IActivitiRunService
{
    @Override
    public Map<String, Object> execute(ProcessCmd cmd)
    {
        return null;
    }
}
