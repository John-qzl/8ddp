package com.cssrc.ibms.dp.form.model;
import java.util.HashMap;
import java.util.Map;

//import com.orient.collaborate.util.Constants;


/**
 * t_cell的Bean
 *@Function Name:  Tcell
 *@Description:
 *@Date Created:  2012-8-20 上午11:43:05
 *@Author: cxk
 *@Last Modified:    ,  Date Modified:
 */
public class Tcell 
{
	private String cellId;
	//#插头名称_1_CELL，cell的名称
	private String cellContent;
	//#插头名称_1_JCX，保存到检查项定义表中的
	private String cellContent_jcx;
	//是否为结果项
	private String isResultItem;
	//结果项类型
	private String resultItemType;
	private String cellOrder;
	//关联的头信息ID，通过列号查找
	private String headId;
	//所属检查表定义ID
	private String cellRefId;
	//所属检查项定义ID
	private String cellRefJCXId;
	private String cellProperty;
	//cell标识 @  #  $
	private String cellFlag;
	//idnex列号
	private int rowInExl;
	//检查项描述
	private String jcxms;
	public String getCellId() {
		return cellId;
	}
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	public String getCellContent() {
		return cellContent;
	}
	public void setCellContent(String cellContent) {
		this.cellContent = cellContent;
	}
	public String getCellContent_jcx() {
		return cellContent_jcx;
	}
	public void setCellContent_jcx(String cellContentJcx) {
		cellContent_jcx = cellContentJcx;
	}
	public String getIsResultItem() {
		return isResultItem;
	}
	public void setIsResultItem(String isResultItem) {
		this.isResultItem = isResultItem;
	}
	public String getResultItemType() {
		return resultItemType;
	}
	public void setResultItemType(String resultItemType) {
		this.resultItemType = resultItemType;
	}
	public String getCellOrder() {
		return cellOrder;
	}
	public void setCellOrder(String cellOrder) {
		this.cellOrder = cellOrder;
	}
	public String getHeadId() {
		return headId;
	}
	public void setHeadId(String headId) {
		this.headId = headId;
	}
	public String getCellRefId() {
		return cellRefId;
	}
	public void setCellRefId(String cellRefId) {
		this.cellRefId = cellRefId;
	}
	public String getCellRefJCXId() {
		return cellRefJCXId;
	}
	public void setCellRefJCXId(String cellRefJCXId) {
		this.cellRefJCXId = cellRefJCXId;
	}	
	public String getCellProperty() {
		return cellProperty;
	}
	public void setCellProperty(String cellProperty) {
		this.cellProperty = cellProperty;
	}
	public String getCellFlag() {
		return cellFlag;
	}
	public void setCellFlag(String cellFlag) {
		this.cellFlag = cellFlag;
	}
	public int getRowInExl() {
		return rowInExl;
	}
	public void setRowInExl(int rowInExl) {
		this.rowInExl = rowInExl;
	}
	public Map<String,String> beanToDataMap()
	{
		//String schemaId =Constants.SCHEMA_ID ;
		String schemaId="001";
	//	String tabId_cell = Constants.CELL_MODELID;
		String tabId_cell="001";
		Map<String,String> dataMap = new HashMap<String, String>();
		dataMap.put("CONTENT_"+tabId_cell, cellContent);
		dataMap.put("IFRESULT_"+tabId_cell, isResultItem);
		dataMap.put("RESULTTYPE_"+tabId_cell, resultItemType);
		dataMap.put("RNUMBER_"+tabId_cell, cellOrder);
		dataMap.put("ITEMDEF_"+schemaId+"_ID", cellRefJCXId);
		dataMap.put("HEADER_"+schemaId+"_ID", headId);
		dataMap.put("TABLE_TEMP_"+schemaId+"_ID", cellRefId);
		return dataMap;
	}
	public String getJcxms() {
		return jcxms;
	}
	public void setJcxms(String jcxms) {
		this.jcxms = jcxms;
	}
}
