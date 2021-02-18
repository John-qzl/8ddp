package com.cssrc.ibms.dp.product.acceptance.bean;

import java.util.List;

public class ProductActual {
	String name;
	String batchName;
	List<ActualData> actualDataList;
	
	public void ProductInfo() {
		
	}
	
	public void ProductInfo(String name,String batchName,List<ActualData> actualDataList) {
		this.name=name;
		this.batchName=batchName;
		this.actualDataList=actualDataList;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public List<ActualData> getActualDataList() {
		return actualDataList;
	}

	public void setActualDataList(List<ActualData> actualDataList) {
		this.actualDataList = actualDataList;
	}
	
}
