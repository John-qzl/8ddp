package com.cssrc.ibms.core.resources.io.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.resources.datapackage.model.DataPackage;
import com.cssrc.ibms.core.resources.io.bean.ins.CheckResult;
import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.dp.form.model.CheckResultCarry;
import com.cssrc.ibms.dp.form.model.CkConditionResult;
import com.cssrc.ibms.dp.sync.bean.PadPhotoInfo;
import com.cssrc.ibms.system.model.SysFile;

@XmlRootElement(name = "tableInstanceBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableInstanceBean {
	
	@XmlElement(name="signResult")
	@XmlElementWrapper(name="signResultList")
	List<SignResult> signResultList;

	TableInstance tableInstance;

	@XmlElement(name="checkResult")
	@XmlElementWrapper(name="checkResultList")
	List<CkConditionResult> ckConditionResultList;

	@XmlElement(name="sysFile")
	@XmlElementWrapper(name="sysFileList")
	List<SysFile> sysFileList;

	//pad拍照及签署表
	@XmlElement(name="padPhotoInfo")
	@XmlElementWrapper(name="padPhotoInfoList")
	List<PadPhotoInfo> padPhotoInfoList;

	public List<PadPhotoInfo> getPadPhotoInfoList() {
		return padPhotoInfoList;
	}

	public void setPadPhotoInfoList(List<PadPhotoInfo> padPhotoInfoList) {
		this.padPhotoInfoList = padPhotoInfoList;
	}

	public List<CkConditionResult> getCkConditionResultList() {
		return ckConditionResultList;
	}

	public void setCkConditionResultList(List<CkConditionResult> ckConditionResultList) {
		this.ckConditionResultList = ckConditionResultList;
	}

	public List<SysFile> getSysFileList() {
		return sysFileList;
	}

	public void setSysFileList(List<SysFile> sysFileList) {
		this.sysFileList = sysFileList;
	}

	public List<SignResult> getSignResultList() {
		return signResultList;
	}

	public void setSignResultList(List<SignResult> signResultList) {
		this.signResultList = signResultList;
	}

	public TableInstance getTableInstance() {
		return tableInstance;
	}

	public void setTableInstance(TableInstance tableInstance) {
		this.tableInstance = tableInstance;
	}
	
	
}