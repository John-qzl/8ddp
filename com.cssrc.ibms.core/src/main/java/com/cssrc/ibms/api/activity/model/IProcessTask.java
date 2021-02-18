package com.cssrc.ibms.api.activity.model;

import java.util.Date;

/**
 * IProcessTask
 * @author liubo
 * @date 2017年5月10日
 */
public interface IProcessTask {

	String getPriority();
	
	void setDescription(String description);
	
	void setExpireDate(Date dueDate);
	
	void setIsReminder(int isReminder);
	
	String getProcessDefinitionId();
	
	String getTaskDefinitionKey();
	
	String getProcessInstanceId();

    String getId();
    String getAssignee();

    void setWarnSet(String warn);

    Date getCreateTime();
}