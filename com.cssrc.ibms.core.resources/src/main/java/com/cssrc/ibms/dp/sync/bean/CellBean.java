package com.cssrc.ibms.dp.sync.bean;

import java.util.HashSet;
import java.util.Set;

public class CellBean implements java.io.Serializable{
	private String cellId;
	private String column;
	private String type;
	private String columnId;
	private String order;
	private String textValue;
	private String cellList;		//本属性用于标识有多少个operation项
	private Set operation = new HashSet(0);
	
	/**
	 * 单元格唯一标记
	 * c1  -   以此为初始值
	 */
	private String key;
	private int colspan;//列合并数 
	private int rowspan;//行合并数
	private String markup; //数据判读标签

	public String getMarkup() {
		return markup;
	}

	public void setMarkup(String markup) {
		this.markup = markup;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getColspan() {
		return colspan;
	}
	public void setColspan(int colspan) {
		this.colspan = colspan;
	}
	public int getRowspan() {
		return rowspan;
	}
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Set getOperation() {
		return operation;
	}
	public void setOperation(Set operation) {
		this.operation = operation;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getTextValue() {
		return textValue;
	}
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	public String getCellId() {
		return cellId;
	}
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	public String getCellList() {
		return cellList;
	}
	public void setCellList(String cellList) {
		this.cellList = cellList;
	}
	
}
