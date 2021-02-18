package com.cssrc.ibms.api.system.intf.worktime;

import java.util.Date;

public interface ICalendarAssignService {
	public abstract Date getRelativeStartTime(String actInstanceId, String nodeId, Integer eventType);

    public abstract Date getTaskTimeByUser(Date baseTime, int deadLine, long longValue);
}
