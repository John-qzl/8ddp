package com.cssrc.ibms.core.log.service;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.intf.ISysJobLogService;
import com.cssrc.ibms.api.log.model.ISysJobLog;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.log.dao.SysJobLogDao;
import com.cssrc.ibms.core.log.model.SysJobLog;
/**
 * 后台任务日志业务层
 * <p>Title:SysJobLogService</p>
 * @author Yangbo 
 * @date 2016-8-4下午05:22:11
 */
@Service
public class SysJobLogService extends BaseService<SysJobLog>
implements ISysJobLogService
{

	@Resource
	private SysJobLogDao dao;

	protected IEntityDao<SysJobLog, Long> getEntityDao()
	{
		return this.dao;
	}

	@Override
	public void addLog(String jobName, String trigName, Date strStartTime,
			Date strEndTime, long runTime, String content, int state) {
		SysJobLog jobLog = new SysJobLog();
		jobLog.setJobName(jobName);
		jobLog.setTrigName(trigName);
		jobLog.setStartTime(strStartTime);
		jobLog.setEndTime(strEndTime);
		jobLog.setRunTime(Long.valueOf(runTime));
		jobLog.setContent(content);
		jobLog.setState(state);
		jobLog.setLogId(UniqueIdUtil.genId());
		this.dao.add(jobLog);
	}


}

