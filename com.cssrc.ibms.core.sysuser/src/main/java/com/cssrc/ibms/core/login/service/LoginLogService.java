package com.cssrc.ibms.core.login.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.login.intf.ILoginLogService;
import com.cssrc.ibms.api.login.model.ILoginLog;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.login.dao.LoginLogDao;
import com.cssrc.ibms.core.login.model.LoginLog;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;

/**
 * 
 * @author Yangbo
 * 2016.7.14
 * LoginlLogService层,保存每次登陆的状态（包括成功和失败）
 *
 */
@Service
public class LoginLogService extends BaseService<LoginLog> implements ILoginLogService{

	@Resource
	private LoginLogDao dao;

	protected IEntityDao<LoginLog, Long> getEntityDao() {
		return this.dao;
	}

	public void save(LoginLog loginLog) {
		Long id = loginLog.getId();
		if ((id == null) || (id.longValue() == 0L)) {
			id = Long.valueOf(UniqueIdUtil.genId());
			loginLog.setId(id);
			add(loginLog);
		} else {
			update(loginLog);
		}
	}
	@Override
	public void save(ILoginLog loginLog) {
		this.save((LoginLog)loginLog);
	}
	public int getTodayFailCount(String account) {
		int failCount = 0;
		Map<String,Object> map = new HashMap();
		Date today = TimeUtil.getDateByDateString(new Date().toLocaleString());
		Date tomorrow = DateUtil.addDay(today, 1);
		map.put("account", account);
		map.put("beginloginTime", today);
		map.put("endloginTime", tomorrow);
		map.put("orderField", "LOGINTIME");
		map.put("orderSeq", "ASC");
		List<LoginLog> loginLogs = this.dao.getBySqlKey("getAll", map);

		for (LoginLog loginLog : loginLogs) {
			if (loginLog.getStatus() == LoginLog.SUCCESS)
				failCount = 0;
			else if (loginLog.getStatus().shortValue() < 0) {
				failCount++;
			}
		}

		return failCount;
	}


}
