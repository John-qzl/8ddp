package com.cssrc.ibms.core.resources.io.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.dp.product.acceptance.bean.WorkBoard;
import com.cssrc.ibms.system.model.SysFile;
@XmlRootElement(name = "transerDataBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class TranserDataBean {
	
	
	@XmlElement(name="transferData")
	@XmlElementWrapper(name="transferDataList")
	List<TransferData> transferDataList;
	
	@XmlElement(name="photoFile")
	@XmlElementWrapper(name="photoFileList")
	List<SysFile> photoFileList=new ArrayList<>();
	
	@XmlElement(name="tableTempBean")
	@XmlElementWrapper(name="tableTempBeanList")
	List<TableTempBean> tableTempBeanList=new ArrayList<>();
	
	public List<TableTempBean> getTableTempBeanList() {
		return tableTempBeanList;
	}

	public void setTableTempBeanList(List<TableTempBean> tableTempBeanList) {
		this.tableTempBeanList = tableTempBeanList;
	}

	public List<SysFile> getPhotoFileList() {
		return photoFileList;
	}

	public void setPhotoFileList(List<SysFile> photoFileList) {
		this.photoFileList = photoFileList;
	}

	public List<TransferData> getTransferDataList() {
		return transferDataList;
	}

	public void setTransferDataList(List<TransferData> transferDataList) {
		this.transferDataList = transferDataList;
	}

	
	
}
