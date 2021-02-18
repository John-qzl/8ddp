package com.cssrc.ibms.api.system.intf;

import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.activity.model.IProcessTask;
import com.cssrc.ibms.api.system.model.BaseInsPortalParams;
import com.cssrc.ibms.api.system.model.ITaskListTab;

public interface BaseIndexShowService {

	/**
	 * 任务列表模块数据
	 * @param params
	 * @return
	 */
	public Map<String,Object> getTaskMap(BaseInsPortalParams params);
	
	/**
	 * 待办任务Tab信息
	 * @return
	 */
	public ITaskListTab pendingMattersTab();
	
	/**
	 * 待办事宜
	 * @param params
	 * @return
	 */
	public List<? extends IProcessTask> pendingMatters(BaseInsPortalParams params,Long userId);
}
