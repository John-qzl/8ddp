package com.cssrc.ibms.api.jms.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.sysuser.model.ISysUser;


public interface IMessageModel {

    Long[] getReceiveUser();

    Long getSendUser();

	Date getSendDate();

	Map<String, String> getTemplateMap();

	String getContent();

	Long getExtId();

	boolean getIsTask();

	String getSubject();

	String getOpinion();

	String[] getTo();

	String[] getBcc();

	String[] getCc();

	void setReceiveUser(Long... receiverUser);
	
	/** 
	* @Title: getFlowVar 
	* @Description: TODO(消息模板支持流程变量) 
	* @param @return     
	* @return Map<String,Object>    返回类型 
	* @throws 
	*/
	Map<String, Object> getVars();

}