package com.cssrc.ibms.api.activity.intf;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.activity.model.IProcessTask;
import com.cssrc.ibms.api.activity.model.ITaskReminder;
import com.cssrc.ibms.api.activity.model.ITaskWarningSet;
import com.cssrc.ibms.core.util.common.WarningSetting;

public interface ITaskReminderService {

	/**
	 * 根据流程定义Id和节点Id获取催办信息。
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public abstract List<?extends ITaskReminder> getByActDefAndNodeId(String actDefId,
			String nodeId);

	/**
	 * 根据流程定义Id获取催办信息。
	 * @param actDefId
	 * @return
	 */
	public abstract List<?extends ITaskReminder> getByActDefId(String actDefId);

	/**
	 * 判断节点是否已经定义催办信息。
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public abstract boolean isExistByActDefAndNodeId(String actDefId,
			String nodeId);

	/**
	 * @author Yangbo 2016-7-22 
	 * @return
	 */
	public abstract List<WarningSetting> getWaringSettingList();

	/**
	 * @author Yangbo 2016-7-22
	 * @return
	 */
	public abstract Map<Integer, WarningSetting> getWaringSetMap();

    /** 
    * @Title: getWarning 
    * @Description: TODO(根据task获取任务的预警信息) 
    * @param @param task     
    * @return void    返回类型 
    * @throws 
    */
    public abstract ITaskWarningSet getWarningSet(IProcessTask task,Date curDate);

}