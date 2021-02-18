package com.cssrc.ibms.core.log.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.ISysErrorLogService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.log.dao.SysErrorLogDao;
import com.cssrc.ibms.core.log.model.SysErrorLog;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
/**
 * 操作错误日志Service层
 * @author Yangbo 2016-7-26
 *
 */
@Service
public class SysErrorLogService extends BaseService<SysErrorLog> implements ISysErrorLogService
{

	@Resource
	private SysErrorLogDao dao;

	protected IEntityDao<SysErrorLog, Long> getEntityDao()
	{
		return this.dao;
	}

	public Long addError(String name, String account, String ip, String error, String errorUrl)
	{
		Long id = Long.valueOf(UniqueIdUtil.genId());
		SysErrorLog sysErrorLog = new SysErrorLog();
		sysErrorLog.setId(id);
		sysErrorLog.setHashcode(String.valueOf(error.hashCode()));
		sysErrorLog.setName(name);
		sysErrorLog.setAccount(account);
		sysErrorLog.setIp(ip);
		sysErrorLog.setError(error);
		sysErrorLog.setErrorurl(StringUtils.substring(errorUrl, 0, 1999));
		sysErrorLog.setErrordate(new Date());
		this.dao.add(sysErrorLog);
		return id;
	}
}

