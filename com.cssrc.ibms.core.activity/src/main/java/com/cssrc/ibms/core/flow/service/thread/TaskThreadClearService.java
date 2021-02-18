package com.cssrc.ibms.core.flow.service.thread;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.ITaskThreadClearService;
import com.cssrc.ibms.core.activity.cache.ActivitiDefCache;

@Service("taskThreadClearService")
public class TaskThreadClearService implements ITaskThreadClearService{

	@Override
	public void clearTaskAll() {
		TaskThreadService.clearAll();
		
	}

	@Override
	public void clearUserAssignAll() {
		TaskUserAssignService.clearAll();
	}

	@Override
	public void clearActivitiDefCache() {
		ActivitiDefCache.clearLocal();		
	}

	
}
