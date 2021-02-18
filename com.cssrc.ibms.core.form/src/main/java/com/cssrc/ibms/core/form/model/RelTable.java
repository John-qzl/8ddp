package com.cssrc.ibms.core.form.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.form.model.IRelTable;

/**
 * 关系表数据
 * @author 
 *
 */
public class RelTable implements IRelTable{
	
	/**
	 * 关联表  表
	 */
	private FormTable relFormTable;
	/**
	 * 关联表  表名
	 */
	private String relTableName;
	/**
	 * 关联表  主键名称。
	 */
	private String relTablePkName="";
	/**
	 * 关联表  显示列。
	 */
	private Map relTablePKColumn;
	/**
	 * 关系表   字段信息。
	 */
	protected List<FormField> relTableFieldList = new ArrayList<FormField>();
	/**
	 * 关系表   数据。
	 * 修改属性relTableDataList将影响 FormUtil.java中的 newRow.before("&lt;#if model.relTab."+tableName+" != null&gt; &lt;
	 * #list model.relTab."+tableName+".relTableDataList as table&gt;"); 中的relTableDataList.
	 * 和web.css中的a.link.relTableDataList,.panel-toolbar a.link.dataList span.
	 */
	private List<Map<String, Object>> relTableDataList=new ArrayList<Map<String,Object>>();
	
	/********************************************************************/
	/**
	 * 主表   信息。
	 */
	private FormTable mainTable;
	/**
	 * 主表   关系列。
	 */
	private FormField mainTableRelField;
	/**
	 * 主表   外键名称。
	 */
	private String mainTableFkName="";
	
	/********************************************************************/
	
	public String getRelTablePkName() {
		return relTablePkName;
	}
	
	public FormTable getRelFormTable() {
		return relFormTable;
	}

	public void setRelFormTable(FormTable relFormTable) {
		this.relFormTable = relFormTable;
	}

	public void setRelTablePkName(String relTablePkName) {
		this.relTablePkName = relTablePkName;
	}
	public List<FormField> getRelTableFieldList() {
		return relTableFieldList;
	}
	public void setRelTableFieldList(List<FormField> relTableFieldList) {
		this.relTableFieldList = relTableFieldList;
	}
	public List<Map<String, Object>> getRelTableDataList() {
		return relTableDataList;
	}
	public void setRelTableDataList(List<Map<String, Object>> relTableDataList) {
		this.relTableDataList = relTableDataList;
	}
	public FormTable getMainTable() {
		return mainTable;
	}
	public void setMainTable(FormTable mainTable) {
		this.mainTable = mainTable;
	}
	public FormField getMainTableRelField() {
		return mainTableRelField;
	}
	public void setMainTableRelField(FormField mainTableRelField) {
		this.mainTableRelField = mainTableRelField;
	}
	public String getMainTableFkName() {
		return mainTableFkName;
	}
	public void setMainTableFkName(String mainTableFkName) {
		this.mainTableFkName = mainTableFkName;
	}


	public String getRelTableName() {
		return relTableName;
	}

	public void setRelTableName(String relTableName) {
		this.relTableName = relTableName;
	}

	public Map getRelTablePKColumn() {
		return relTablePKColumn;
	}

	public void setRelTablePKColumn(Map relTablePKColumn) {
		this.relTablePKColumn = relTablePKColumn;
	}
	
	//往relTableDataList中加数据行
	public void addRow(Map<String, Object> row){
		this.relTableDataList.add(row);
	}
}