package com.cssrc.ibms.core.resources.io.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.resources.datapackage.model.DataPackage;
import com.cssrc.ibms.dp.product.acceptance.bean.WorkBoard;
import com.cssrc.ibms.dp.sync.bean.Conventional;
import com.cssrc.ibms.system.model.SysFile;
@XmlRootElement(name = "transferData")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferData {
	//策划
	AcceptancePlan acceptancePlan;
	//工作项看板
	WorkBoard WorkBoard;
	//数据包
	@XmlElement(name="dataPackage")
	@XmlElementWrapper(name="dataPackageList")
	List<DataPackage> dataPackageList;
	//实例
	@XmlElement(name="tableInstanceBean")
	@XmlElementWrapper(name="tableInstanceBeanList")
	List<TableInstanceBean> tableInstanceBeanList;
	//策划的文件
	@XmlElement(name="fileData")
	@XmlElementWrapper(name="fileDataList")
	List<FileData> fileDataList;
	//???
	@XmlElement(name="productInfoBean")
	@XmlElementWrapper(name="productInfoBeanList")
	List<ProductInfoBean> productInfoBeanList;
	//???
	@XmlElement(name="conventional")
	@XmlElementWrapper(name="pConventionalList")
	List<Conventional> pConventionalList;
	


	
	public List<Conventional> getpConventionalList() {
		return pConventionalList;
	}


	public void setpConventionalList(List<Conventional> pConventionalList) {
		this.pConventionalList = pConventionalList;
	}


	public List<FileData> getFileDataList() {
		return fileDataList;
	}


	public void setFileDataList(List<FileData> fileDataList) {
		this.fileDataList = fileDataList;
	}


	public List<ProductInfoBean> getProductInfoBeanList() {
		return productInfoBeanList;
	}


	public void setProductInfoBeanList(List<ProductInfoBean> productInfoBeanList) {
		this.productInfoBeanList = productInfoBeanList;
	}


	public AcceptancePlan getAcceptancePlan() {
		return acceptancePlan;
	}


	public void setAcceptancePlan(AcceptancePlan acceptancePlan) {
		this.acceptancePlan = acceptancePlan;
	}


	public List<DataPackage> getDataPackageList() {
		return dataPackageList;
	}


	public void setDataPackageList(List<DataPackage> dataPackageList) {
		this.dataPackageList = dataPackageList;
	}


	public List<TableInstanceBean> getTableInstanceBeanList() {
		return tableInstanceBeanList;
	}


	public void setTableInstanceBeanList(List<TableInstanceBean> tableInstanceBeanList) {
		this.tableInstanceBeanList = tableInstanceBeanList;
	}


	public WorkBoard getWorkBoard() {
		return WorkBoard;
	}


	public void setWorkBoard(WorkBoard workBoard) {
		WorkBoard = workBoard;
	}
	
	
	
}
