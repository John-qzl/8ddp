package com.cssrc.ibms.core.user.event;

import java.util.Map;

import com.cssrc.ibms.api.activity.model.IProcessCmd;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * 
 * <p>Title:EventUtil</p>
 * @author Yangbo 
 * @date 2016-8-18下午03:47:34
 */
public class EventUtil {
	public static void publishNodeSqlEvent(String actDefId, String nodeId,
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

	public static void publishTriggerNewFlowEvent(String action, String nodeId,
			IProcessCmd processCmd) {
		TriggerNewFlowEvent tiNewFlowEvent = new TriggerNewFlowEvent(
				new TriggerNewFlowModel(action, nodeId, processCmd));
		AppUtil.publishEvent(tiNewFlowEvent);
	}
}
