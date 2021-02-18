package com.cssrc.ibms.core.flow.util;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.cssrc.ibms.api.jms.intf.IMessageHandler;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.flow.status.FlowStatusService;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;


public class FlowUtil {
    private static FlowStatusService flowStatusService=AppUtil.getBean(FlowStatusService.class);
	/**
	 * 判断账号为空。
	 * 
	 * @param assignee
	 * @return
	 */
	public static boolean isAssigneeEmpty(String assignee) {
		if (StringUtil.isEmpty(assignee)
				|| BpmConst.EMPTY_USER.equals(assignee)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断任务执行人非空。
	 * 
	 * @param assignee
	 * @return
	 */
	public static boolean isAssigneeNotEmpty(String assignee) {
		return !isAssigneeEmpty(assignee);
	}
	
	/**
	 * 获取默认勾选的消息类型的key字符串,逗号分割。
	 * 
	 * @return
	 */
	public static String getDefaultSelectInfoType() {
		StringBuffer result = new StringBuffer();
		@SuppressWarnings("unchecked")
		Map<String, IMessageHandler> map = (Map<String, IMessageHandler>) AppUtil
				.getBean("handlersMap");
		Set<Map.Entry<String, IMessageHandler>> set = map.entrySet();
		for (Iterator<Map.Entry<String, IMessageHandler>> it = set.iterator(); it
				.hasNext();) {
			Map.Entry<String, IMessageHandler> entry = (Map.Entry<String, IMessageHandler>) it
					.next();
			if (entry.getValue().getIsDefaultChecked() == true) {
				if (result != null && result.length() > 0)
					result.append(",");
				result.append(entry.getKey());
			}
		}
		return result.toString();
	}
	

	/**
	 * 获取任务或实例页面处理路径。
	 * 
	 * @param id
	 * @param isTask
	 * @return
	 */
	public static String getUrl(String id, boolean isTask) {
		if (BeanUtils.isEmpty(id))
			return "";
		String url = AppConfigUtil.get("serverUrl");
		if (isTask) {
			url += "/oa/task/toStart.do?taskId=" + id;
		} else {
			url += "/oa/task/processRun/info.do?runId=" + id;
		}
		return url;
	}
	
	
	/**
	 * 返回按钮物理的路径。
	 * @return
	 */
	public static String getDesignButtonPath(){
	    return SysConfConstant.CONF_ROOT+File.separator+"flow"+ File.separator;
	}

    public static String getTaskStatus(Short checkStatus, int i)
    {
        return flowStatusService.getTaskStatus(checkStatus,i);
    }
}
