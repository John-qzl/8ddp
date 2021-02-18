package com.cssrc.ibms.dp.sync.bean;

import java.util.HashSet;
import java.util.Set;

public class RowsBean implements java.io.Serializable{
	private Set row = new HashSet(0);
	private String rowsid;
	
	public String getRowsid() {
		return rowsid;
	}

	public void setRowsid(String rowsid) {
		this.rowsid = rowsid;
	}

	public Set getRow() {
		return row;
	}

	public void setRow(Set row) {
		this.row = row;
	}

}
