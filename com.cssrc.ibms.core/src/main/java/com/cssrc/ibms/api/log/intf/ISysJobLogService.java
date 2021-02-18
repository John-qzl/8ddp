package com.cssrc.ibms.api.log.intf;


import java.util.Date;
import java.util.List;
import com.cssrc.ibms.api.log.model.ISysJobLog;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public abstract interface ISysJobLogService
 {

	public abstract void addLog(String jobName, String trigName,
			Date strStartTime, Date strEndTime, long runTime, String content,
			int state);

	public abstract List<? extends ISysJobLog> getAll(QueryFilter queryFilter);

	public abstract void delByIds(Long[] lAryId);
}

