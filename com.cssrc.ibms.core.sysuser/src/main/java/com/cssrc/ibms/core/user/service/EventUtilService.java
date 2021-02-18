package com.cssrc.ibms.core.user.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.model.IProcessCmd;
import com.cssrc.ibms.api.sysuser.intf.IEventUtilService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.user.event.NodeSqlContext;
import com.cssrc.ibms.core.user.event.NodeSqlEvent;
import com.cssrc.ibms.core.user.event.TriggerNewFlowEvent;
import com.cssrc.ibms.core.user.event.TriggerNewFlowModel;
import com.cssrc.ibms.core.user.event.UserEvent;
import com.cssrc.ibms.core.user.event.UserPositionEvent;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * EventUtilService
 * @author liubo
 * @date 2017年2月17日
 */
@Service
public class EventUtilService implements IEventUtilService{
	
	public void publishNodeSqlEvent(String actDefId, String nodeId,
			String action, Map<String, Object> dataMap) {
		NodeSqlContext context = new NodeSqlContext();
		context.setActDefId(actDefId);
		context.setNodeId(nodeId);
		context.setAction(action);
		context.setDataMap(dataMap);
		NodeSqlEvent event = new NodeSqlEvent(context);
		AppUtil.publishEvent(event);
	}

	public static void publishUserEvent(Long userId, int action, ISysUser user) {
		UserEvent ev = new UserEvent(user.getUserId());
		ev.setAction(action);
		ev.setUser(user);
		AppUtil.publishEvent(ev);
	}
    
    public static void publishUserPositionEvent(Long userId, int action, IUserPosition userPos)
    {
        UserPositionEvent ev = new UserPositionEvent(userPos.getPosId());
        ev.setAction(action);
        ev.setUserPosition(userPos);
        AppUtil.publishEvent(ev);
    }
	   
	public void publishTriggerNewFlowEvent(String action, String nodeId,
			IProcessCmd processCmd) {
		TriggerNewFlowEvent tiNewFlowEvent = new TriggerNewFlowEvent(
				new TriggerNewFlowModel(action, nodeId, processCmd));
		AppUtil.publishEvent(tiNewFlowEvent);
	}
}
