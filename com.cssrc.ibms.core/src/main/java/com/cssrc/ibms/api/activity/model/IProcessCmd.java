package com.cssrc.ibms.api.activity.model;

import java.util.Map;

public interface IProcessCmd {

	/** 
	* @Title: getFormDataMap 
	* @Description: TODO(获取表单数据) 
	* @param @return    
	* @return Map    返回类型 
	* @throws 
	*/
	Map getFormDataMap();

	/** 
	* @Title: getBusinessKey 
	* @Description: TODO(获取业务PK) 
	* @param @return     
	* @return String    返回类型 
	* @throws 
	*/
	String getBusinessKey();

	Object getTransientVar(String key);

	/** 
	* @Title: setBusinessKey 
	* @Description: TODO(设置业务PK) 
	* @param @param string     
	* @return void    返回类型 
	* @throws 
	*/
	void setBusinessKey(String string);
	/** 
	* @Title: getActDefId 
	* @Description: TODO(获取流程定义ID) 
	* @param @return     
	* @return String    流程定义ID
	* @throws 
	*/
	String getActDefId();
	
}