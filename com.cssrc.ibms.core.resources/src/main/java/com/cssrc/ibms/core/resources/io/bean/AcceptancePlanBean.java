package com.cssrc.ibms.core.resources.io.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.resources.datapackage.model.DataPackage;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.bean.acceptanceReport;
@XmlRootElement(name = "acceptancePlanBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class AcceptancePlanBean {

	AcceptancePlan acceptancePlan;
	
	@XmlElement(name="acceptanceGroup")
	@XmlElementWrapper(name="acceptanceGroupList")
	List<AcceptanceGroup> acceptanceGroupList;
	
	@XmlElement(name="tableInstanceBean")
	@XmlElementWrapper(name="tableInstanceBeanList")
	List<TableInstanceBean> tableInstanceBeanList;
	
	
	@XmlElement(name="dataPackage")
	@XmlElementWrapper(name="dataPackageList")
	List<DataPackage> dataPackageList;
	
	@XmlElement(name="acceptanceReport")
	@XmlElementWrapper(name="acceptanceReportList")
	List<acceptanceReport> acceptanceReportList;
	

	
	public List<DataPackage> getDataPackageList() {
		return dataPackageList;
	}

	public void setDataPackageList(List<DataPackage> dataPackageList) {
		this.dataPackageList = dataPackageList;
	}

	public List<acceptanceReport> getAcceptanceReportList() {
		return acceptanceReportList;
	}

	public void setAcceptanceReportList(List<acceptanceReport> acceptanceReportList) {
		this.acceptanceReportList = acceptanceReportList;
	}



	public AcceptancePlan getAcceptancePlan() {
		return acceptancePlan;
	}

	public void setAcceptancePlan(AcceptancePlan acceptancePlan) {
		this.acceptancePlan = acceptancePlan;
	}

	public List<AcceptanceGroup> getAcceptanceGroupList() {
		return acceptanceGroupList;
	}

	public void setAcceptanceGroupList(List<AcceptanceGroup> acceptanceGroupList) {
		this.acceptanceGroupList = acceptanceGroupList;
	}

	public List<TableInstanceBean> getTableInstanceBeanList() {
		return tableInstanceBeanList;
	}

	public void setTableInstanceBeanList(List<TableInstanceBean> tableInstanceBeanList) {
		this.tableInstanceBeanList = tableInstanceBeanList;
	}
	
}
