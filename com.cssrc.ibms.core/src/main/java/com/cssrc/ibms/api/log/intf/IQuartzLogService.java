package com.cssrc.ibms.api.log.intf;

import java.util.Date;

public interface IQuartzLogService {

	void addLog(String content, String trigName, String jobName,
			Long runTime, Date strStartTime, int state, Date strEndTime);

}
