package com.cssrc.ibms.api.activity.intf;

import java.util.Map;
/**
 * 任务信息显示扩展
 * @author scc
 * 
 */
public interface IPendingTaskExtService {
	/**
	 * 可对鼠标悬停事件时显示的数据data进行修改
	 * @param tableId 业务表的ID 在ibms_form_table表中，根据此tableId可查到对应的具体业务表
	 * @param data  需要显示的所有数据，其中包括的 f_businessKey 为 tableId 对应的业务表中的数据主键
	 * @return data 显示的数据
	 */
	public Map<String,Object> customData(Long tableId,Map<String,Object> data);
}
