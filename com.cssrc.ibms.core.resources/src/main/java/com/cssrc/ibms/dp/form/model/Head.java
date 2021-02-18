package com.cssrc.ibms.dp.form.model;
import java.util.HashMap;
import java.util.Map;

//import com.orient.collaborate.util.Constants;


/**
 * t_head的Bean
 *@Function Name:  Head
 *@Description:
 *@Date Created:  2012-8-20 下午12:12:23
 *@Author: cxk
 *@Last Modified:    ,  Date Modified:
 */
public class Head 
{
	private String headSign = "";
	/*
	 * 头信息所在的Exl中的列号，插头等多列的  保存第一列的列号
	 * 给cell插入headId信息做关联字段
	 */
	private int rowInExl = 0;
	private String headId = "";
	//检查表定义ID
	private String headRefId = "";
	private String headName = "";
	private String headOrder = "";
	public String getHeadSign() {
		return headSign;
	}
	public void setHeadSign(String headSign) {
		this.headSign = headSign;
	}
	public int getRowInExl() {
		return rowInExl;
	}
	public void setRowInExl(int rowInExl) {
		this.rowInExl = rowInExl;
	}
	public String getHeadId() {
		return headId;
	}
	public void setHeadId(String headId) {
		this.headId = headId;
	}
	public String getHeadRefId() {
		return headRefId;
	}
	public void setHeadRefId(String headRefId) {
		this.headRefId = headRefId;
	}
	public String getHeadName() {
		return headName;
	}
	public void setHeadName(String headName) {
		this.headName = headName;
	}
	public String getHeadOrder() {
		return headOrder;
	}
	public void setHeadOrder(String headOrder) {
		this.headOrder = headOrder;
	}
	
	/*public Map<String,String> beanToDataMap()
	{
		String schemaId = Constants.SCHEMA_ID;
		String tabId_header = Constants.HEADER_MODELID;
		Map<String,String> dataMap = new HashMap<String, String>();
		dataMap.put("ID", headId);
		
		int index = -1;
		if((index=headName.indexOf("[产品名称]"))!=-1)
			headName = headName.substring(0, index);

		dataMap.put("NAME_"+tabId_header, headName);
		dataMap.put("TABLE_TEMP_"+schemaId+"_ID", headRefId);
		dataMap.put("ORDER_"+tabId_header, headOrder);
		return dataMap;
	}*/
}
