package com.cssrc.ibms.core.form.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 主表的外键表信息
 * @author 
 *
 */
public class FkTable{
	
	/**
	 * 主表的外键表  表
	 */
	private FormTable fkFormTable;
	/**
	 *  主表的外键表  表名
	 */
	private String fkTableName;
	/**
	 * 主表的外键表  主键名称。
	 */
	private String fkTablePkName="";
	/**
	 * 主表的外键表  显示列。
	 */
	private Map fkTablePKColumn;
	/**
	 * 主表的外键表   字段信息。
	 */
	protected List<FormField> fkTableFieldList = new ArrayList<FormField>();
	/**
	 * 主表的外键表   数据。
	 */
	private List<Map<String, Object>> fkTableDataList=new ArrayList<Map<String,Object>>();
	
	/********************************************************************/
	/**
	 * 主表   信息。
	 */
	private FormTable mainTable;
	/**
	 * 主表   外键列。
	 */
	private FormField mainTableFkField;
	/**
	 * 主表   外键名称。
	 */
	private String mainTableFkName="";
	
	/********************************************************************/
	
	public FormTable getFkFormTable() {
		return fkFormTable;
	}
	public void setFkFormTable(FormTable fkFormTable) {
		this.fkFormTable = fkFormTable;
	}
	public String getFkTableName() {
		return fkTableName;
	}
	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}
	public String getFkTablePkName() {
		return fkTablePkName;
	}
	public void setFkTablePkName(String fkTablePkName) {
		this.fkTablePkName = fkTablePkName;
	}
	public Map getFkTablePKColumn() {
		return fkTablePKColumn;
	}
	public void setFkTablePKColumn(Map fkTablePKColumn) {
		this.fkTablePKColumn = fkTablePKColumn;
	}
	public List<FormField> getFkTableFieldList() {
		return fkTableFieldList;
	}
	public void setFkTableFieldList(List<FormField> fkTableFieldList) {
		this.fkTableFieldList = fkTableFieldList;
	}
	public List<Map<String, Object>> getFkTableDataList() {
		return fkTableDataList;
	}
	public void setFkTableDataList(List<Map<String, Object>> fkTableDataList) {
		this.fkTableDataList = fkTableDataList;
	}
	public FormTable getMainTable() {
		return mainTable;
	}
	public void setMainTable(FormTable mainTable) {
		this.mainTable = mainTable;
	}
	public FormField getMainTableFkField() {
		return mainTableFkField;
	}
	public void setMainTableFkField(FormField mainTableFkField) {
		this.mainTableFkField = mainTableFkField;
	}
	public String getMainTableFkName() {
		return mainTableFkName;
	}
	public void setMainTableFkName(String mainTableFkName) {
		this.mainTableFkName = mainTableFkName;
	}
	
	
	
}