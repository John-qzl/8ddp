package com.cssrc.ibms.core.log.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.intf.IQuartzLogService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.log.dao.QuartzLogDao;
import com.cssrc.ibms.core.log.model.QuartzLog;

@Service
public class QuartzLogService extends BaseService<QuartzLog> implements
		IQuartzLogService {

	@Resource
	private QuartzLogDao dao;

	@Override
	protected IEntityDao<QuartzLog, Long> getEntityDao() {
		return dao;
	}

	@Override
	public void addLog(String content, String trigName, String jobName,
			Long runTime, Date strStartTime, int state, Date strEndTime) {
		QuartzLog model = new QuartzLog();
		Long id = UniqueIdUtil.genId();
		model.setLogId(id);
		model.setJobName(jobName);
		model.setTrigName(trigName);
		model.setStartTime(strStartTime);
		model.setEndTime(strEndTime);
		model.setContent(content);
		model.setState(state);
		model.setRunTime(runTime);
		dao.add(model);
	}

}
